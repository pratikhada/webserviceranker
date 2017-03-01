/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webservice.wsdl.implementation;

/**
 *
 * @author BIKASH
 */
public class Parameter {
    private String name;
    private String type;
    private Object value;
    private Parameter children[];

    public Parameter(){
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return type;
    }
    public void setChildren(Parameter [] children){
        this.children = children;
    }
    public Parameter[] getChildren(){
        return children;
    }
    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue(){
        return value;
    }

    public Parameter[] getLeafChildren(){
        Parameter[] leaves = null;
        if (children != null && children.length != 0){
            Parameter[][] temp = new Parameter[children.length][];
            int leafCount = 0;
            for (int i=0; i< children.length; i++){
                temp[i] = children[i].getLeafChildren();
                leafCount += temp[i].length;
            }
            leaves = new Parameter[leafCount];
            int index = 0;
            for (int i=0; i<temp.length; i++)
                for (int j=0; j<temp[i].length; j++)
                    leaves[index++] = temp[i][j];
        }else{
            //If this node itself is a leaf
            leaves = new Parameter[]{this};
        }
        return leaves;
    }

    public void printDetail(){
        System.out.println("          Parameter = "+name);
        System.out.println("          Parameter type = "+type);
        System.out.println("          Parameter value = "+value);
        if(children!=null){
            System.out.println("   -------ChildParameter = ");
            for(int i=0; i<children.length; i++)
                children[i].printDetail();
        }

    }
}
