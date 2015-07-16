package groovy

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;

def frame = new JFrame("��ʼ���ؼ�");
def textfield = new JTextField();
def button = new JButton("����");
def area = new JTextArea(30,100);
def scrollPane = new JScrollPane(area); 

frame.setBounds(200, 100, 1000, 600);
frame.add(scrollPane,BorderLayout.CENTER);
frame.add(button,BorderLayout.EAST);
frame.setVisible(true); 
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

button.addActionListener{
    actionEvent->
    def maps = start(area.getText().toString().trim())
    area.setText("")
    printResult(maps , area)
}

def start(file_name){
    return parseNode(new LinkedHashMap() ,new XmlParser(false,false).parse(new File("E:/xiaocoder/android_studio_files/qlk2-0/app/src/main/res/layout/"+file_name)))
}

def parseNode(maps,node){
    getLines(maps,node)
    def nodes = node.children();
    nodes.each{
        if(!(it instanceof String)){
             parseNode(maps,it)
        }
    }
    return maps;
}

def getLines(maps, node){
   if(node){
       def id = node.attribute("android:id")
       if(id){ 
          maps.put(id.substring(id.lastIndexOf("/")+1,id.length()).trim(),node.name())
       }
   }
}

def printResult(maps , area){
     maps.each{
         key,value->
         area.append( "${value} ${key};"+"\r\n")
     }
    area.append("\r\n");
     maps.each{
       key,value->
        area.append("${key} = getViewById(R.id.${key});"+"\r\n")
     }
}

  



