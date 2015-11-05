package groovy

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class GuiView{

    public static void main(String[] args){
        def guiview = new GuiView()
        guiview.init()
    }
         
    def frame = new JFrame("初始化控件");
    def textfield = new JTextField();
    def button = new JButton("解析")
    def area = new JTextArea(30,100);
    def scrollPane = new JScrollPane(area); 
      
    def init(){
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
    }

    def start(file_name){
        return parseNode(new LinkedHashMap() ,new XmlParser(false,false).parse(new File(file_name)))
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

	     if(!key.equals("xc_id_model_layout")&&!key.equals("xc_id_model_titlebar") && !key.equals("xc_id_model_content") && !key.equals("xc_id_model_no_net")){
		area.append( "${value} ${key};"+"\r\n") 
	     }
             
         }
         
         area.append("\r\n");
         maps.each{
           key,value->

	     if(!key.equals("xc_id_model_layout")&&!key.equals("xc_id_model_titlebar") && !key.equals("xc_id_model_content") && !key.equals("xc_id_model_no_net")){
		area.append("${key} = getViewById(R.id.${key});"+"\r\n")
	   }
            
         }
         
         area.append("\r\n");
         maps.each{
           key,value->

	     if(!key.equals("xc_id_model_layout")&&!key.equals("xc_id_model_titlebar") && !key.equals("xc_id_model_content") && !key.equals("xc_id_model_no_net")){
		area.append("${key} = (${value})findViewById(R.id.${key});"+"\r\n")
	   }
            
         }
         
         area.append("\r\n");
         maps.each{
           key,value->

	     if(!key.equals("xc_id_model_layout")&&!key.equals("xc_id_model_titlebar") && !key.equals("xc_id_model_content") && !key.equals("xc_id_model_no_net")){
		area.append("holder.${key} = (${value})convertView.findViewById(R.id.${key});"+"\r\n")
	   }
            
         }
    }
}
  