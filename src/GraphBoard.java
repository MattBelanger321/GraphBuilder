import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.*;

@SuppressWarnings("unchecked")
public class GraphBoard extends JFrame implements Global {
    private final JPanel modePanel;
    private final JPanel funcPanel;
    private final Graph graph;
    private final JButton save;
    private final JButton load;
    private final String FILE_PATH = "saveFile.graph";

    public GraphBoard() {
        super("Graph Builder");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.red);

        //initalize graph object and add it to the window
        this.graph = new Graph();
        this.add(this.graph);

        //creates menu panel at the top
        this.modePanel = new JPanel();
        this.modePanel.setLayout(null);
        this.modePanel.setBounds(0, 0, 175, 50);
        this.modePanel.setBackground(Color.gray);

        //Component Selection
        modes.setSize(100, 30);
        modes.addItem("NODES");
        modes.addItem("EDGES");
        this.modePanel.add(modes);
        this.add(this.modePanel);
        this.funcPanel = new JPanel();
        this.funcPanel.setBounds(175, 0, 150, 50);
        this.funcPanel.setBackground(Color.gray);
        this.funcPanel.setLayout(null);

        //Function Selection
        functions.addItem("ADD");
        functions.addItem("DELETE");
        functions.addItem("MOVE");
        functions.setSize(100, 30);
        this.funcPanel.add(functions);
        this.add(this.funcPanel);

        //Save/Load Button
        JPanel savePanel = new JPanel();
        savePanel.setBounds(325, 0, 75, 50);
        savePanel.setBackground(Color.gray);
        this.add(savePanel);
        JPanel loadPanel = new JPanel();
        loadPanel.setBounds(400, 0, 100, 50);
        loadPanel.setBackground(Color.gray);
        this.add(loadPanel);
        this.save = new JButton("SAVE");
        savePanel.add(this.save);
        this.load = new JButton("LOAD");
        loadPanel.add(this.load);
        this.addButtonListeners();
        this.setVisible(true);

        //Add GUI Logic
        this.addMenuListeners();
    }

    private void addButtonListeners() {
        this.save.addActionListener((actionEvent) -> {
            JFrame textWindow = new JFrame("POPUP");
            textWindow.setBounds(600, 0, 100, 200);
            this.saveGraph(this.graph.getNodes(), this.graph.getEdges());
            JTextArea ta = new JTextArea();
            ta.setText("SAVE SUCCESSFUL");
            textWindow.add(ta);
            textWindow.setVisible(true);
        });
        this.load.addActionListener(actionEvent -> {
            Scanner read;

            try {
                read = new Scanner(new FileInputStream(FILE_PATH));
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
                return;
            }

            LinkedList<int[]> readNodes;
            readNodes = new LinkedList<>();

            int currentVal;
            for(currentVal = read.nextInt(); currentVal != -1; currentVal = read.nextInt()) {
                int[] tempx = new int[]{currentVal, 0};
                currentVal = read.nextInt();
                tempx[1] = currentVal;
                readNodes.add(tempx);
            }

            LinkedList<int[]> readEdges;
            readEdges = new LinkedList<>();

            for(currentVal = read.nextInt(); currentVal != -1; currentVal = read.nextInt()) {
                int[] temp = new int[]{currentVal, 0};
                currentVal = read.nextInt();
                temp[1] = currentVal;
                readEdges.add(temp);
            }

            read.close();
            GraphBoard.this.graph.setGraph(readNodes, readEdges);
        });
    }

    private void saveGraph(LinkedList<int[]> nodes, LinkedList<int[][]> edges) {
        PrintWriter out;

        try {
            out = new PrintWriter(new File(FILE_PATH));
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
            return;
        }

        Iterator n = nodes.iterator();

        while(n.hasNext()) {
            int[] node = (int[])n.next();
            out.write(String.format("%d %d\n", node[0], node[1]));
        }

        out.write("-1\n");
        n = edges.iterator();

        while(n.hasNext()) {
            int[][] edge = (int[][])n.next();
            out.write(String.format("%d %d\n", nodes.indexOf(edge[0]), nodes.indexOf(edge[1])));
        }

        out.write("-1\n");
        out.close();
    }

    private void addMenuListeners() {
        this.modePanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            public void mousePressed(MouseEvent mouseEvent) {
            }

            public void mouseReleased(MouseEvent mouseEvent) {
            }

            public void mouseEntered(MouseEvent mouseEvent) {
                if (Global.modes.getSelectedItem().equals("EDGES")) {
                    Global.functions.removeItem("MOVE");
                } else if (Global.functions.getItemCount() != 3) {
                    Global.functions.addItem("MOVE");
                }

            }

            public void mouseExited(MouseEvent mouseEvent) {
                if (Global.modes.getSelectedItem().equals("EDGES")) {
                    Global.functions.removeItem("MOVE");
                } else if (Global.functions.getItemCount() != 3) {
                    Global.functions.addItem("MOVE");
                }

            }
        });
        this.funcPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            public void mousePressed(MouseEvent mouseEvent) {
            }

            public void mouseReleased(MouseEvent mouseEvent) {
            }

            public void mouseEntered(MouseEvent mouseEvent) {
                if (Objects.equals(Global.modes.getSelectedItem(), "EDGES")) {
                    Global.functions.removeItem("MOVE");
                } else if (Global.functions.getItemCount() != 3) {
                    Global.functions.addItem("MOVE");
                }

            }

            public void mouseExited(MouseEvent mouseEvent) {
                if (Objects.equals(Global.modes.getSelectedItem(), "EDGES")) {
                    Global.functions.removeItem("MOVE");
                } else if (Global.functions.getItemCount() != 3) {
                    Global.functions.addItem("MOVE");
                }

            }
        });
    }
}
