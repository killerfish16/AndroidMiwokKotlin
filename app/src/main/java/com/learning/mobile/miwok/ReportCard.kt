package com.learning.mobile.miwok

class ReportCard(var name:String, var age:Int, var sex:String, var grades: Map<Int,String>)


fun main(args: Array<String>){
  var reportCardStd1 = ReportCard("Raj", 25, "male", mapOf(2010 to "A", 2011 to "B"))

  print(reportCardStd1.age)
  print(reportCardStd1.grades)

}