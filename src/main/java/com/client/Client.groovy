package com.client

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class Client {
    public static void main(String[] args) {
        String ip = System.in.newReader().readLine()
        while (true) {
            def s = new Socket(ip, 6666)
            s.withStreams { input, output ->
                while (true) {
                    //String msg = System.in.newReader().readLine()
                    def response = [
                            type:"",
                            msg:"no"
                    ]
                    def msg = JsonOutput.toJson(response)
                    println JsonOutput.prettyPrint(msg)
                    output.newObjectOutputStream().writeObject(msg)
                    def buffer = input.newReader().readLine()
                    println "response = $buffer"
                }
            }
        }
    }
}
