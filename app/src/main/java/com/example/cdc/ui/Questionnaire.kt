package com.example.cdc.ui

open class Question() {
    var questionList = mutableListOf<String>("ID", "name", "temperature")
}

class Questionnaire() : Question() {
    var answerList = mutableListOf<String>("", "", "")
    val safety: Boolean
        get() = this.checkSafe()
    val id: String
        get() = this.getid()
    val name: String
        get() = this.getname()
    fun getAttribute(attribute: String): String{
        var attr = ""
        for (i in questionList.indices){
            if (questionList[i] == attribute)
                attr = answerList[i]
        }
        return attr
    }
    private fun checkSafe(): Boolean{
        var safety = true
        val temperature = getAttribute("temperature").toDoubleOrNull()
        if(temperature == null || temperature >= 37.3)
            safety = false
        return safety
    }
    private fun getid(): String{
        return getAttribute("ID")
    }
    private fun getname(): String{
        return getAttribute("姓名")
    }
}