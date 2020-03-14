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
            while (true) {
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
            //Если выпала очередь данного клиента
            case 'NewTurn':
                handleTurn(responseMap)
                break
        }
    }

    def void handleInit(response) {
        EvenUser = response['even']
        Ops = normalizeOutput(response['ops'])
        currentMagic = response['val']
        EvenTurn = false
        uiForm.InitVals(Ops,currentMagic)
    }

    def normalizeOutput(def InputList){
        def list = new ArrayList<String>()
        InputList.each { it->
            def s = it[0] + "${it[1].toString()[0] == '-' ?'('+it[1]+')':it[1]}"
            list.add(s)
        }
        return list
    }

    def void handleTurn(response) {
        EvenTurn = response['Turn']
        uiForm.UpdateNumber(response['MagicNumber'])
        uiForm.EnableButtons(true)
    }
}
