package com.example.cdc.ui

open class Question() {
    var questionList = mutableListOf<String>("体温(℃)", "7天内是否有省外旅居史(Y/N)", "是否有发热咳嗽等症状(Y/N)", "核酸检测报告是否呈阴性(Y/N)", "所在小区是否有新冠阳性病例(Y/N)")
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
        val temperature = getAttribute("体温（℃）").toDoubleOrNull()
        if (temperature!! > 37.3) safety = false
        for (i in questionList.indices){
            var attr: String
            if (questionList[i] != "体温（℃）")
            {
                attr = answerList[i]
                if(attr == "Y")
                    safety = false
            }
        }
        return safety
    }
    private fun getid(): String{
        return getAttribute("ID")
    }
    private fun getname(): String{
        return getAttribute("姓名")
    }
}