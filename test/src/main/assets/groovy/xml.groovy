package groovy


def file_name = "fragment_healthy_card.xml" 

def maps = parseNode(new LinkedHashMap() ,new XmlParser(false,false).parse(new File("E:/xiaocoder/android_studio_files/qlk2-0/app/src/main/res/layout/"+file_name)))

printResult(maps)

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

def printResult(maps){
     maps.each{
         key,value->
         println("${value} ${key};");
     }
     println("---------------------------------------------------------------------------------------------");
     maps.each{
       key,value->
       println("${key} = getViewById(R.id.${key});")
     }
}




// groovy���Դ��Ŀ������ò��ֻ�б�ǩ�ڵ���ı��ڵ����֣�groovy�е�ע�ͺ����Զ�����ڵ�
def parseNode1(Node node){
    // ��ӡ�ڵ�����ԣ������Կ����Ǳ�ǩ�ڵ�Ҳ�������ı��ڵ�ģ�������ı��ڵ㣬������Ϊ��
    println(node.attributes())
    def nodes = node.children();
    // ����nodes
    nodes.each{
        if(!(it instanceof String)){
             // ���notes�Ǳ�ǩ�ڵ�
             parseNode(it)
        }else{
             // ���notes���ı��ڵ㣬��ô�ı��ڵ�.each���it��string�ַ�����text������
             println("�ı��ڵ�-->"+it);
        }
    }
}



// �ж��Ǳ�ǩ�ڵ㻹���ı��ڵ� �� groovy���Դ��Ŀ������ò��ֻ�б�ǩ�ڵ���ı��ڵ����֣�groovy�е�ע�ͺ����Զ�����ڵ�
def parseNode2(node){
    def nodes = node.children();
    if(nodes instanceof String){
        // ��ʾnode��һ���ı��ڵ�
        return
    }else{
        // ��ʾnode��һ����ǩ�ڵ�
        // ��ӡ�ñ�ǩ�ڵ�node������
        println(node.attributes())
        // ������node�ڵ��µ��ӽڵ�
        // ������ı��ڵ㣬��ôeach�����string�ַ�����text������
        nodes.each{
            if(!(it instanceof String)){
                 parseNode(it)
            }
        }
    }
}
