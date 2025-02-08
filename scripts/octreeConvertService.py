from flask import Flask, request, jsonify
import uuid
from concurrent.futures import ThreadPoolExecutor
import pandas as pd
import math
import numpy as np
import sys
import os
import json
import traceback




convertionThreadPool = ThreadPoolExecutor(max_workers=4)
app = Flask(__name__)
convertTasks = {}





class OctreeNode:
    def __init__(self,originalX,originalY,originalZ,dimision,index,attributesCount,outputDir,indexPrefix) -> None:
        self.children = [None for i in range(0,8)]
        self.originalX = originalX
        self.originalY = originalY
        self.originalZ = originalZ
        self.dimision = dimision
        self.index = index
        self.recordsNums = 0
        self.data = []
        self.hasWrittenToDisk = False
        self.attributesCount = attributesCount
        self.outputDir = outputDir
        self.indexStr = f"{indexPrefix}{index}"



    def getStarCount(self):
        return self.recordsNums

    def clearData(self):
        self.data.clear()
    
    
    def memoryUsage(self):

        length = len(self.data)
        if length<1:
            return 0
        return sys.getsizeof(self.data[0])*length
    


    def insert(self,row):
        for i in range(len(row)):
            self.data.append(row[i])
        self.recordsNums+=1

        

    def writeToDisk(self):

        if len(self.data)<1:
            return
        
        mode = "wb"
        if self.hasWrittenToDisk:
            mode = "ab"

        self.hasWrittenToDisk = True
        f = open(f"{self.outputDir}/{self.indexStr}.bin",mode)
        buffer = np.array(self.data,np.float32)
        buffer.tofile(f)
        f.close()
        self.clearData()



class OctreeManager:
    def __init__(self,max_dist,max_stars_node,attributes_nums,maxInMemory,outputDirPath) -> None:
        self.max_dist = max_dist
        self.max_stars_node = max_stars_node
        self.attributes_nums = attributes_nums
        bound_box = -max_dist/2
        self.maxInMemory = maxInMemory
        self.outputDirPath = outputDirPath
        self.root = OctreeNode(bound_box,bound_box,bound_box,math.ceil(max_dist),0,attributes_nums,outputDirPath,'')
        self.leafNodes = {
            "0":self.root
        }


    def calculateChildrenIndex(self,node,x,y,z):
        childrenIdx = 0
        halfDimision = node.dimision/2
        if x>node.originalX+halfDimision:
            childrenIdx+=1
        if y>node.originalY+halfDimision:
            childrenIdx+=4
        if z>node.originalZ+halfDimision:
            childrenIdx+=2
        return childrenIdx
    

    def findInsertNode(self,node,insertX,insertY,insertZ):

        if not node.children[0]:
            return node

        return self.findInsertNode(node.children[self.calculateChildrenIndex(node,insertX,insertY,insertZ)],insertX,insertY,insertZ)

    def splitNode(self,node):
        del self.leafNodes[node.indexStr]
        for i in range(8):
            originalX = node.originalX
            originalY = node.originalY
            originalZ = node.originalZ
            halfD = node.dimision/2
            
            if i%2 != 0:
                originalX+=halfD
            if i>=4:
                originalY+=halfD
            if i==2 or i==3 or i==6 or i == 7:
                originalZ+=halfD
            
            node.children[i] = OctreeNode(originalX,originalY,originalZ,halfD,i,self.attributes_nums,self.outputDirPath,node.indexStr)
            self.leafNodes[node.children[i].indexStr] = node.children[i]



        if node.hasWrittenToDisk:
            nodeFilePath = f"{self.outputDirPath}/{node.indexStr}.bin"
            buffer = np.fromfile(nodeFilePath,dtype=np.float32).reshape(-1,self.attributes_nums)

            for row in buffer:
                insertNode = self.findInsertNode(node,row[0],row[1],row[2])
                insertNode.insert(row)
            os.remove(nodeFilePath)

        for i in range(0,len(node.data),self.attributes_nums):
            row = node.data[i:i+self.attributes_nums]
            insertedNode = self.findInsertNode(node,row[0],row[1],row[2])
            insertedNode.insert(row)

        node.clearData()
            

    def writeNodesToDisk(self,nodeList):
        nodeList.sort(key=lambda x:x[1],reverse=True)
        writeToDiskAmount = 0
        for v,usage in nodeList:
            if writeToDiskAmount+usage < self.maxInMemory/2:
                writeToDiskAmount+=usage
                v.writeToDisk()
            else:
                break
        

    def insert(self,row):

        
        
        node = self.findInsertNode(self.root,row[0],row[1],row[2])
        if node.getStarCount()>self.max_stars_node:
            self.splitNode(node)
            node = self.findInsertNode(node,row[0],row[1],row[2])
        node.insert(row)
        memoryUsageSum = 0
        maxUsage = -1
        maxUsageNode = None
        nodeList = []
        for k,v in self.leafNodes.items():
            usage = v.memoryUsage()
            memoryUsageSum+=usage
            nodeList.append((v,usage))

        if memoryUsageSum >= self.maxInMemory:
            self.writeNodesToDisk(nodeList)


    def serilize(self):
        indexList = []
        for k,v in self.leafNodes.items():
            v.writeToDisk()
            indexList.append(v.indexStr)
        indexOutput = {
            "maxDist":self.max_dist,
            "index": indexList
        }

        jsonFile = open(f"{self.outputDirPath}/index.json","w",encoding="utf-8")
        json.dump(indexOutput,jsonFile,indent=4)

        

        



MAX_IN_MEMORY = 50*1024*1024
def csvOctreeCovert(task):
    chunkSize = 1e5
    maxDistance = 0
    cols = -1


    selectedColumns = []

    df = pd.read_csv(task["filePath"],nrows=1)
    selectedColumns.extend(df.columns[:3].to_list())

    del df


    attributeMapping =task["attributeMapping"]
    attributes = ["bv","lum","absMag","vx","vy","vz","speed"]
    for attribute in attributes:
        if attributeMapping[attribute]:
            selectedColumns.append(attributeMapping[attribute])
        else:
            selectedColumns.append(attribute)

    
    # unit pc
    for chunk in pd.read_csv(task["filePath"],chunksize=chunkSize):
        cols = chunk.shape[1]
        if cols<3:
            return False
        for row in chunk.iloc[:,:3].values:
            maxDistance = max(maxDistance, math.ceil(abs(np.max(row))))

    
    octreeManager = OctreeManager(maxDistance*2,3e4,len(attributes)+3,MAX_IN_MEMORY,task["outputDir"])


    
    for chunk in pd.read_csv(task["filePath"],chunksize=chunkSize):

        for col in selectedColumns:
            if col not in chunk.columns:
                chunk[col] = 1  # 为不存在的列添加默认值 1

        chunk = chunk[selectedColumns]
        for row in chunk.values:        
            octreeManager.insert(row)
    octreeManager.serilize()
    return True
    


            
        





def sparkOctreeCovert(task):
    pass



handleMap = {
    "csv":csvOctreeCovert,
    "spark":sparkOctreeCovert
}


def convertToOctree(taskId):
    try:
        task = convertTasks[taskId]
        success = handleMap[task["format"]](task)
        if not success:
            convertTasks[taskId]["status"]|=0b10
        convertTasks[taskId]["status"]|=0b01
    except Exception as e:
        task["status"]|=0b11
        traceback.print_exc()
        
    




@app.route('/createOctreeConvertionTask',methods=['post'])
def createConvertionTask():
    requestParams = request.get_json()

    
    convertionTaskId = str(uuid.uuid4())
    convertTasks[convertionTaskId] = {
        "filePath":requestParams["filePath"],
        "format":requestParams["fileFormat"],
        "attributeMapping":requestParams["attributeMapping"],
        "outputDir":requestParams["outputDir"],
        "status":0b00
    }

    convertionThreadPool.submit(convertToOctree,convertionTaskId)


    return {"taskId":convertionTaskId}


@app.route("/queryConvertionTask",methods=['post'])
def queryConvertionTask():
    requestParams = request.get_json()
    taskId = requestParams["taskId"]
    if taskId in convertTasks:
        return {
        "status":convertTasks[taskId]["status"]
    }

    return {
        "error":"not Found"
    }



if __name__ == '__main__':
    app.run()