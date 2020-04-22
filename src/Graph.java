import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class Graph extends JPanel implements Global {
    private LinkedList<int[]> nodes; //[0] for x, [1] for y
    private LinkedList<int[][]> edges; //Every edge is an array of 2 nodes [0] for head, [1] for tail

    private int nodeIndex;
    private int node[];
    private int tempPoints[] = null;

    private JPopupMenu nodeMenu;
    private JPopupMenu edgeMenu;

    private final int NODE_RADIUS = 5;

    public Graph(){
        setBounds(0,50,500,450);
        setOpaque(false);
        nodes = new LinkedList<>();
        edges = new LinkedList<>();

        nodeMenu = new JPopupMenu();
        JMenuItem addN = new JMenuItem("ADD");
        addN.addActionListener(actionEvent -> functions.setSelectedIndex(0));
        nodeMenu.add(addN);
        JMenuItem deleteN = new JMenuItem("DELETE");
        deleteN.addActionListener(actionEvent -> functions.setSelectedIndex(1));
        nodeMenu.add(deleteN);
        JMenuItem moveN = new JMenuItem("MOVE");
        try{
            moveN.addActionListener(actionEvent -> functions.setSelectedIndex(2));
        }catch(IndexOutOfBoundsException | NullPointerException ignored){}
        nodeMenu.add(moveN);

        edgeMenu = new JPopupMenu();
        JMenuItem addE = new JMenuItem("ADD");
        addE.addActionListener(actionEvent -> functions.setSelectedIndex(0));
        edgeMenu.add(addE);
        JMenuItem deleteE = new JMenuItem("DELETE");
        deleteE.addActionListener(actionEvent -> functions.setSelectedIndex(1));
        edgeMenu.add(deleteE);
        addGraphListener();
    }

    private int nodeClicked(MouseEvent me){
        int i = 0;
        for(int[] xy: nodes){
            if(xy[0]-NODE_RADIUS-3<=me.getX() && xy[0]+NODE_RADIUS+3>= me.getX() && xy[1]-NODE_RADIUS-3<=me.getY() && xy[1]+NODE_RADIUS+3>= me.getY())
                return i;
            i++;
        }
        return -1;
    }

    private void addGraphListener(){
        addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getButton() != 3){
                    if(modes.getSelectedItem().equals("EDGES")){
                        if(functions.getSelectedItem().equals("DELETE")){
                            int[][] clickedEdge = edgeClicked(me);
                            if(clickedEdge != null){
                                edges.remove(clickedEdge);
                                repaint();
                            }
                        }
                    }
                }else{
                    if(modes.getSelectedItem().equals("NODES")){
                        nodeMenu.show(me.getComponent(), me.getX(), me.getY());
                    }else if(modes.getSelectedItem().equals("EDGES")){
                        edgeMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                nodeIndex = nodeClicked(me);
                node = nodeIndex>=0?nodes.get(nodeIndex):null;
                if(modes.getSelectedItem().equals("NODES") && me.getButton() != 3){
                    if(functions.getSelectedItem().equals("ADD") && node == null){
                        nodes.add(new int[]{me.getX(), me.getY()});
                        repaint();
                    }else if(functions.getSelectedItem().equals("DELETE") &&  node != null){
                        nodes.remove(node);
                        repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                int node2[];
                try{
                    node2 = nodes.get(nodeClicked(me));
                }catch(IndexOutOfBoundsException e){
                    node2 = null;
                }
                if(modes.getSelectedItem().equals("EDGES")){
                    if(functions.getSelectedItem().equals("ADD")){
                        if(node != null && tempPoints != null && node2 != null){
                            edges.add(new int[][]{node,node2});
                        }
                    }
                }
                node = null;
                tempPoints = null;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent me) {
                if(node != null && modes.getSelectedItem().equals("NODES") && functions.getSelectedItem().equals("MOVE")){
                    node[0] = me.getX();
                    node[1] = me.getY();
                    nodes.remove(nodeIndex);
                    nodes.add(nodeIndex,node);
                    repaint();
                }else if(modes.getSelectedItem().equals("EDGES")){
                    if(functions.getSelectedItem().equals("ADD")){
                        if(node != null){
                            tempPoints = new int[2];
                            tempPoints[0] = me.getX();
                            tempPoints[1] = me.getY();
                            repaint();
                        }else{
                            tempPoints = null;
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if(modes.getSelectedItem().equals("EDGES")){
                    functions.removeItem("MOVE");
                }else{
                    if(functions.getItemCount() != 3)
                        functions.addItem("MOVE");
                }
            }
        });
    }

    private int[][] edgeClicked(MouseEvent me){
        for(int[][] edge: edges){
            double m = slope(edge);
            double b = yInt(edge[0],m);
            if(me.getY()+5 >= m*(me.getX())+b && me.getY()-5 <= m*(me.getX())+b){
                return edge;
            }
        }
        return null;
    }

    private double yInt(int[] point, double slope){
        return point[1] - slope*point[0];
    }

    private double slope(int[][] edge){
        return (edge[1][1] - edge[0][1])/(double)(edge[1][0] - edge[0][0]);
    }

    public LinkedList<int[]> getNodes(){return nodes;}

    public LinkedList<int[][]> getEdges(){return edges;}

    public void setGraph(LinkedList<int[]> newNodes,LinkedList<int[]> newEdges){
        nodes = newNodes;
        edges.clear();
        for(int[] edge: newEdges){
            edges.add(new int[][]{nodes.get(edge[0]),nodes.get(edge[1])});
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(modes.getSelectedItem().equals("EDGES")){
            if(functions.getSelectedItem().equals("ADD")){
                if(node != null && tempPoints != null){
                    g.drawLine(node[0],node[1],tempPoints[0],tempPoints[1]);
                }
            }
        }

        for(int[] xy: nodes){
            g.fillOval(xy[0]-NODE_RADIUS,xy[1]-NODE_RADIUS,NODE_RADIUS*2,NODE_RADIUS*2);
        }

        for(int i = 0; i<edges.size();i++){
            int node[][] = edges.get(i);
           if(nodes.contains(node[0]) && nodes.contains(node[1])){
                g.drawLine(node[0][0],node[0][1],node[1][0],node[1][1]);
            }else{
              edges.remove(node);
               i--;
               continue;
            }
        }
    }
}