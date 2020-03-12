package com.client

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class Client {


    private final def PORT = 6666

    private Boolean EvenUser
    private ArrayList<String> Ops
    private Integer currentMagic
    private Boolean EvenTurn
    private UIForm uiForm


    Socket serverSocket

    Client(String ip) {
        this.serverSocket = new Socket(ip,PORT)
    }

    Client(uiForm,String ip) {
        this.uiForm = uiForm
        this.serverSocket = new Socket(ip, PORT)
    }

    void setUiForm(UIForm uiForm) {
        this.uiForm = uiForm
    }

    def start(){
        while (true) {
            serverSocket.withStreams { input, output ->
                def response = input.newObjectInputStream().readObject()
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
            default:
                break
        }
    }

    def void handleInit(response) {
        EvenUser = response['even']
        Ops = normalizeOutput(response['ops'])
        currentMagic = response['MagicNumber']
        EvenTurn = false
        uiForm.InitVals(Ops,currentMagic)
        checkTurn()

    }

    def normalizeOutput(def TupleList){
        def list = new ArrayList<String>()
        TupleList.each { it->
            def s = it[0] + "${it[1].toString()[0] == '-' ?'('+it[1]+')':it[1]}"
            list.add(s)
        }
        return list
    }

    def checkTurn(){
        if(EvenUser == EvenTurn)
            uiForm.EnableButtons()
    }


}
