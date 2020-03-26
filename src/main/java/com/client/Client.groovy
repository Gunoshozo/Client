package com.client

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class Client extends Thread{


    private final def PORT = 6666

    private Boolean EvenUser
    private ArrayList<String> Ops
    private Integer currentMagic
    private Boolean EvenTurn
    private UIForm uiForm
    private ShouldRun = true

    private def outputStream
    private def inputStream

    Socket serverSocket

    Boolean getEvenUser() {
        return EvenUser
    }

    Socket getServerSocket() {
        return serverSocket
    }

    void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket
    }

    Client(String ip) {
        this.serverSocket = new Socket(ip,PORT)
    }

    void setUiForm(UIForm uiForm) {
        this.uiForm = uiForm
    }

    void run(){
        serverSocket.withStreams { input, output ->
            inputStream = input
            outputStream = output
            while (ShouldRun) {
                def response = input.newReader().readLine()
                handleResponse(response)
            }
        }
    }

    def handleResponse(response){
        def jsonSlurper = new JsonSlurper()
        def responseMap = jsonSlurper.parseText(response)
        switch(responseMap['type']){
            case 'Init':
                handleInit(responseMap)
                break
            case 'NewTurn':
                handleTurn(responseMap)
                break
            case 'End':
                endGame(responseMap)
                break
        }
    }

    def void handleInit(response) {
        EvenUser = response['even']
        Ops = normalizeOutput(response['ops'])
        currentMagic = response['val']
        EvenTurn = false
        uiForm.InitVals(Ops,currentMagic)
        handleTurn(response)
    }

    def normalizeOutput(def InputList){
        def list = new ArrayList<String>()
        InputList.each { it->
            def s = it[0] + "${it[1].toString()[0] == '-' ?'('+it[1]+')':it[1]}"
            list.add(s)
        }
        return list
    }

    def handleTurn(response) {
        EvenTurn = response['turn']
        uiForm.UpdateNumber(response['val'])
        if (EvenTurn == EvenUser)
            uiForm.EnableButtons(true)
    }

    def sentOper(opNumber) {
        def data = [type:"move",
                    oper:opNumber]
        def json = JsonOutput.toJson(data)+'\n'
        outputStream << json
    }

    def endGame(response){
        uiForm.UpdateNumber(response['val'])
        uiForm.EndGame(response['turn'] == EvenUser)
        ShouldRun = false
    }
}
