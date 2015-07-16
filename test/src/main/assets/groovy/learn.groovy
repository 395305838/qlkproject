package groovy
// Groovy�е����б������Ƕ�������������ʱ��Groovy��Ҫ��ǿ����������,����Ҫ��ʹ�ùؼ���def
// Groovy��ķ���Ĭ����public��

def repeat_print(content , repeat_print=5){
    for( i in 0..repeat_print){
        println(content);
    }
}


repeat_print("hello" , 2);
repeat_print("word" );

// �ַ�������
def var = """  Hello World, Groovy \" \\ """  
println var                  

// ѭ��
for(i = 0; i<5; i++){  
    println i  
} 

def count_list = [1,2,3,4,2,2]
def count = count_list.size();
count_list.each{
    println it + 1
}

for( i in count_list){
    println i +"--"
}

// �ַ���֧�ֵ�������
def str = "Hello World!"  

println str[2]
println str[(str.length()-1)..(str.length()-1)]  
println str[-1..0]



def numbers = [1,2,3,4,2,2]
assert numbers.count(2) == 3
println(numbers)

numbers.join("--")

assert ["JAVA", "GROOVY"] == 
  ["Java", "Groovy"]*.toUpperCase()
  
//  ʵ���ϣ��հ��Ǹ����󣬿�����Ϊ�������ݣ�Ҳ�͵�������,������Ϊ�����ķ���ֵ���е���Java�е��ڲ��ࡣ
 // �հ��ǿ�ִ�еĴ����{}�����ǲ���Ҫ���ƣ������ڶ���֮��ִ��
 // �հ���һ������飬�����ܹ���Ϊ�������д��ݣ�Groovy �еĺ����򷽷�������������
 
 def list = ["java" , "groovy" , "c++"]
 println list[-1] //���������1��Ԫ��
// println list.get(-1) // ����
println list.get(1);
 
 list.each{
     println it
 }
 
 list.each{
  value->  println  value
 }
 
def hash = [name:"Andy", "abc":45]
hash.each{ key, value ->
 println "${key} : ${value}"
}

// �հ���û�У����� 
// �հ�������Ϊ��������
def excite = { word ->
 return "${word}!!"
//    return word+"!!" // ��������һ����
}

def excite2 = {
    return it + "!!"
}

// ��δ�������Ϊ excite �ıհ�������հ�����һ����������Ϊ word�������ص� String �� word ������������̾�š���ע���� String ʵ�����滻 ���÷���
//�� String ��ʹ�� ${value}�﷨������ Groovy �滻 String �е�ĳ��������ֵ�����Խ�����﷨���� return word + "!!" �Ŀ�ݷ�ʽ��
//�ӳ�ִ��
//��Ȼ���˱հ�������͸�ʵ��ʹ�����ˡ�����ͨ�����ַ������ñհ���ֱ�ӵ��û���ͨ�� call() �������á�
//����ʹ�� ClosureExample �࣬�ڱհ�������������������д��룺
assert "Groovy!!" == excite("Groovy")
assert "Java!!" == excite.call("Java")
// ���Կ��������ֵ��÷�ʽ���ܹ���������ֱ�ӵ��õķ�������ࡣ��Ҫ���Ǳհ��� Groovy ��Ҳ��һ����� �� �ȿ�����Ϊ�������ݣ�Ҳ���Է����Ժ�ִ�С�

def sng2
// ? ��ʾ�ж�null
println sng2?.toUpperCase()

def var2= "HelloWorld"  
println "${var2},${var2.class}"  
println var2+var2
var2 = true  
println "${var2},${var2.class}"  

// �ɱ����
def sum(int...vars){  
    def total = 0  
    for(i in vars){  
        total += i  
    }  
    return total  
}  
println sum(1,2,3)

def maps = ["name":"xiaoming" , "age":"20"];
maps.school = "guangzhou"
//println maps;
//maps.each{
// println it
//}
maps.each{
    key ,value->
    println key +" " +value;
}
println "-------------------"
for( i in maps){
    println i
    println i.class
    // ���������������entry
}

maps.each { println it.getKey()+"->"+it.getValue() }
println "-------------------"

// ���Ӽ�ֵ��
maps << [WA:'Washington']

def collect={
    it+1
}
// valu��һ���µ�ֵ
def test =  [1, 2, 3]
value =test.collect { it * 2 }
assert value == [2, 4, 6]
println test

[1, 2, 3].each { it * 2 }


def hehe = {
    key1 , key2,key3->
    println key1 + 100+key2+key3;
}


[1,2,3].each{

    hehe(it,2,6) // hehe(1,2,3,4) hehe(1,2)���ᱨ��
}

value = [1, 2, 3].findAll { it > 1 }
assert value == [2, 3]

value = [1, 2, 3].findAll { it > 1 }
assert value == [2, 3]

value = [1, 2, 3].every { it < 5 }
assert value
 
value = [1, 2, 3].every { item | item < 3 }
assert ! value

value = [1, 2, 3].any { it > 2 }
assert value

enum Day{
SUNDAY, MONDAY, TUESDAY,WEDNESDAY,
THURSDAY, FRIDAY,SATURDAY
}
def today = Day.SUNDAY
switch(today){
case [Day.SATURDAY,Day.SUNDAY]:
println 'Weekends'
break
case Day.MONDAY..Day.FRIDAY:
println 'Workdays'
break
default:
println 'Not a valid Day!'
}
// ע�⣬switch��case�п���ʹ���κζ���case�л�����ʹ��List�͡���Χ��


//��̬��
//Groovy���еĶ�����һ��Ԫ��metaClass������ͨ�������metaClass���Է��ʸ�Ԫ�࣬���ǿ���ͨ��Ԫ�������̬��ӷ������鿴��������Ժͷ��������䣩


// ��̬���һ�������� �հ���
def msg = "hello"
msg.metaClass.up = { msg.toUpperCase()}
msg.up();
msg.metaClass.down = {hehe,enen->
 hehe.toUpperCase()+enen
 }
 
// ��groovy�����ж��󶼿��Ա�ǿ��תΪΪһ��booleanֵ������ null, void ���ֵ �� ����Ϊ false, �����Ķ���Ϊ true.
// ���Բ�Ҫ��д ���ִ����ˣ�if (name != null && name.length > 0) {} ��Ϊ�� if (name) {}
 
msg.down("ceshi","123")


//l ��ӡ�������Ժͷ�����
msg.metaClass.metaMethods.each { println it }
msg.metaClass.properties.each { println it }
//l �������Ժͷ�����
if( msg.metaClass.respondsTo (msg, "up")){
println msg.up()


try{
    // ...
}catch(any) {
    // something bad happens
}
}

if( msg.metaClass.hasProperty(msg,"bytes")){
println msg.bytes.encodeBase64()
}

class Bean{
    def age;
}

Bean he = new Bean();
he.metaClass.name = "hehe";
println he.toString();

println he.name



//println new File("C:\\Users\\fengjingyu\\Desktop\\groovyfile.txt").text

def file;
try{
file = new File("E:\\xiaocoder\\groovy_files\\groovytest.txt");
file.append("123456789")
}catch (Exception e){
    println "error"
}


file.eachLine{
    println it
}

file.splitEachLine(" "){
    words->
    words.each{
        println it+"--"
    }
}

file.eachLine{
str,lineNumber->  
println str
println lineNumber
}

file.splitEachLine(",") {println it  }  


//server.name = application.name
//server.status = status
//server.sessionCount =3
//server.start()
//server.stop()
//
//server.with {
//    name = application.name
//    status = status
//    sessionCount =3
//    start()
//    stop()
//}

// groovy�� ==����equals�Ĺ��ܣ��һ����Ա����ָ���쳣

"abcde".each{
    println it +"-"
}

// ��ȡ��ǰĿ¼��Ŀ¼�б�
def dir2 = new File(".")
dir2.eachFile{file->
  println file
}

def dir = new File(".")
dir.eachFile{file->
  if(file.isFile()){
    println "FILE: ${file}"    
  }else if(file.isDirectory()){
    println "DIR:  ${file}"
  }else{
    println "Uh, I'm not sure what it is..."
  }
}

// ��ӡһ����ҳ
//"http://www.ibm.com".toURL().eachLine{ println it }��
def url = new URL("http://www.baidu.com")
url.eachLine{line->
  println line
}


