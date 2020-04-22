import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GraphBoard extends JFrame implements Global{
    private JPanel modePanel;
    private JPanel funcPanel;
    private Graph graph;
    private JPanel savePanel;
    private JPanel loadPanel;
    private JButton save;
    private JButton load;

    private final String FILE_PATH = "saveFile.graph";

    public GraphBoard(){
        super("Graph Builder");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.red);

        graph = new Graph();
        add(graph);

        //Mode JPanel
        modePanel = new JPanel();
        modePanel.setLayout(null);
        modePanel.setBounds(0,0,175,50);
        modePanel.setBackground(Color.gray);
        //ComboBox
        modes.setSize(100,30);
        modes.addItem("NODES");
        modes.addItem("EDGES");
        modePanel.add(modes);
        add(modePanel);

        //Function JPanel
        funcPanel = new JPanel();
        funcPanel.setBounds(175,0,150,50);
        funcPanel.setBackground(Color.gray);
        funcPanel.setLayout(null);
        //ComboBox
        functions.addItem("ADD");
        functions.addItem("DELETE");
        functions.addItem("MOVE");
        functions.setSize(100,30);
        funcPanel.add(functions);
        add(funcPanel);

        //Save Load
        savePanel = new JPanel();
        savePanel.setBounds(325,0,75,50);
        savePanel.setBackground(Color.gray);
        add(savePanel);
        loadPanel = new JPanel();
        loadPanel.setBounds(400,0,100,50);
        loadPanel.setBackground(Color.gray);
        add(loadPanel);
        save = new JButton("SAVE");
        savePanel.add(save);
        load = new JButton("LOAD");
        loadPanel.add(load);
        addButtonListeners();

        setVisible(true);
        addMenuListeners();
    }

    private void addButtonListeners(){
        save.addActionListener(actionEvent -> {
            JFrame textWindow = new JFrame("POPUP");
            textWindow.setBounds(600,0,100,200);
            saveGraph(graph.getNodes(),graph.getEdges());

            JTextArea ta = new JTextArea();
            ta.setText("SAVE SUCCESSFUL");
            textWindow.add(ta);
            textWindow.setVisible(true);
        });

        load.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Scanner read = null;
                try {
                    read = new Scanner(new FileInputStream(FILE_PATH));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                LinkedList<int[]> readNodes = new LinkedList<>();
                int currentVal = read.nextInt();
                while(currentVal != -1){
                    int[] temp = new int[2];
                    temp[0] = currentVal;
                    currentVal = read.nextInt();
                    temp[1] = currentVal;
                    readNodes.add(temp);
                    currentVal = read.nextInt();
                }

                LinkedList<int[]> readEdges = new LinkedList<>();
                currentVal = read.nextInt();
                while(currentVal != -1){
                    int[] temp = new int[2];
                    temp[0] = currentVal;
                    currentVal = read.nextInt();
                    temp[1] = currentVal;
                    readEdges.add(temp);
                    currentVal = read.nextInt();
                }
                read.close();
                graph.setGraph(readNodes,readEdges);
            }
        });
    }

    private void saveGraph(LinkedList<int[]> nodes,LinkedList<int[][]> edges){
        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(FILE_PATH));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        for(int[] node: nodes){
            out.write(String.format("%d %d\n",node[0],node[1]));
        }
        out.write("-1\n");
        for(int[][] edge: edges){
            out.write(String.format("%d %d\n",nodes.indexOf(edge[0]),nodes.indexOf(edge[1])));
        }
        out.write("-1\n");

        out.close();
    }

    private void addMenuListeners(){
        modePanel.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(modes.getSelectedItem().equals("EDGES")){
                    functions.removeItem("MOVE");
                }else{
                    if(functions.getItemCount() != 3)
                        functions.addItem("MOVE");
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(modes.getSelectedItem().equals("EDGES")){
                    functions.removeItem("MOVE");
                }else{
                    if(functions.getItemCount() != 3)
                        functions.addItem("MOVE");
                }
            }
        });

        funcPanel.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                if(modes.getSelectedItem().equals("EDGES")){
                    functions.removeItem("MOVE");
                }else{
                    if(functions.getItemCount() != 3)
                        functions.addItem("MOVE");
                }
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
                if(modes.getSelectedItem().equals("EDGES")){
                    functions.removeItem("MOVE");
                }else{
                    if(functions.getItemCount() != 3)
                        functions.addItem("MOVE");
                }
            }
        });

    }


}