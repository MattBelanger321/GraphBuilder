import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

@SuppressWarnings("ALL")
public class Graph extends JPanel implements Global {
    private LinkedList<int[]> nodes;
    private final LinkedList<int[][]> edges;
    private int nodeIndex;
    private int[] node;
    private int[] tempPoints = null;
    private final JPopupMenu nodeMenu;
    private final JPopupMenu edgeMenu;
    private final int NODE_RADIUS = 5;

    public Graph() {
        this.setBounds(0, 50, 500, 450);
        this.setOpaque(false);
        this.nodes = new LinkedList<>();
        this.edges = new LinkedList<>();
        this.nodeMenu = new JPopupMenu();
        JMenuItem addN = new JMenuItem("ADD");
        addN.addActionListener((actionEvent) -> {
            functions.setSelectedIndex(0);
        });
        this.nodeMenu.add(addN);
        JMenuItem deleteN = new JMenuItem("DELETE");
        deleteN.addActionListener((actionEvent) -> {
            functions.setSelectedIndex(1);
        });
        this.nodeMenu.add(deleteN);
        JMenuItem moveN = new JMenuItem("MOVE");

        try {
            moveN.addActionListener((actionEvent) -> {
                functions.setSelectedIndex(2);
            });
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
        }

        this.nodeMenu.add(moveN);
        this.edgeMenu = new JPopupMenu();
        JMenuItem addE = new JMenuItem("ADD");
        addE.addActionListener((actionEvent) -> {
            functions.setSelectedIndex(0);
        });
        this.edgeMenu.add(addE);
        JMenuItem deleteE = new JMenuItem("DELETE");
        deleteE.addActionListener((actionEvent) -> {
            functions.setSelectedIndex(1);
        });
        this.edgeMenu.add(deleteE);
        this.addGraphListener();
    }

    private int nodeClicked(MouseEvent me) {
        int i = 0;

        for(Iterator var3 = this.nodes.iterator(); var3.hasNext(); ++i) {
            int[] xy = (int[])var3.next();
            if (xy[0] - 5 - 3 <= me.getX() && xy[0] + 5 + 3 >= me.getX() && xy[1] - 5 - 3 <= me.getY() && xy[1] + 5 + 3 >= me.getY()) {
                return i;
            }
        }

        return -1;
    }

    private void addGraphListener() {
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() != 3) {
                    if (Objects.equals(Global.modes.getSelectedItem(), "EDGES") && Objects.equals(Global.functions.getSelectedItem(), "DELETE")) {
                        int[][] clickedEdge = Graph.this.edgeClicked(me);
                        if (clickedEdge != null) {
                            Graph.this.edges.remove(clickedEdge);
                            Graph.this.repaint();
                        }
                    }
                } else if (Objects.equals(Global.modes.getSelectedItem(), "NODES")) {
                    Graph.this.nodeMenu.show(me.getComponent(), me.getX(), me.getY());
                } else if (Objects.equals(Global.modes.getSelectedItem(), "EDGES")) {
                    Graph.this.edgeMenu.show(me.getComponent(), me.getX(), me.getY());
                }

            }

            public void mousePressed(MouseEvent me) {
                Graph.this.nodeIndex = Graph.this.nodeClicked(me);
                Graph.this.node = Graph.this.nodeIndex >= 0 ? (int[])Graph.this.nodes.get(Graph.this.nodeIndex) : null;
                if (Objects.equals(Global.modes.getSelectedItem(), "NODES") && me.getButton() != 3) {
                    if (Objects.equals(Global.functions.getSelectedItem(), "ADD") && Graph.this.node == null) {
                        Graph.this.nodes.add(new int[]{me.getX(), me.getY()});
                        Graph.this.repaint();
                    } else if (Objects.equals(Global.functions.getSelectedItem(), "DELETE") && Graph.this.node != null) {
                        Graph.this.nodes.remove(Graph.this.node);
                        Graph.this.repaint();
                    }
                }

            }

            public void mouseReleased(MouseEvent me) {
                int[] node2;
                try {
                    node2 = (int[])Graph.this.nodes.get(Graph.this.nodeClicked(me));
                } catch (IndexOutOfBoundsException var4) {
                    node2 = null;
                }

                if (Objects.equals(Global.modes.getSelectedItem(), "EDGES") && Objects.equals(Global.functions.getSelectedItem(), "ADD") && Graph.this.node != null && Graph.this.tempPoints != null && node2 != null) {
                    Graph.this.edges.add(new int[][]{Graph.this.node, node2});
                }

                Graph.this.node = null;
                Graph.this.tempPoints = null;
                Graph.this.repaint();
            }

            public void mouseEntered(MouseEvent mouseEvent) {
            }

            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent me) {
                if (Graph.this.node != null && Objects.equals(Global.modes.getSelectedItem(), "NODES") && Objects.equals(Global.functions.getSelectedItem(), "MOVE")) {
                    Graph.this.node[0] = me.getX();
                    Graph.this.node[1] = me.getY();
                    Graph.this.nodes.remove(Graph.this.nodeIndex);
                    Graph.this.nodes.add(Graph.this.nodeIndex, Graph.this.node);
                    Graph.this.repaint();
                } else if (Objects.equals(Global.modes.getSelectedItem(), "EDGES") && Objects.equals(Global.functions.getSelectedItem(), "ADD")) {
                    if (Graph.this.node != null) {
                        Graph.this.tempPoints = new int[2];
                        Graph.this.tempPoints[0] = me.getX();
                        Graph.this.tempPoints[1] = me.getY();
                        Graph.this.repaint();
                    } else {
                        Graph.this.tempPoints = null;
                    }
                }

            }

            public void mouseMoved(MouseEvent mouseEvent) {
                if (Objects.equals(Global.modes.getSelectedItem(), "EDGES")) {
                    Global.functions.removeItem("MOVE");
                } else if (Global.functions.getItemCount() != 3) {
                    Global.functions.addItem("MOVE");
                }

            }
        });
    }

    private int[][] edgeClicked(MouseEvent me) {
        Iterator var2 = this.edges.iterator();

        int[][] edge;
        double m;
        double b;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            edge = (int[][])var2.next();
            m = this.slope(edge);
            b = this.yInt(edge[0], m);
        } while((double)(me.getY() + 5) < m * (double)me.getX() + b || (double)(me.getY() - 5) > m * (double)me.getX() + b);

        return edge;
    }

    private double yInt(int[] point, double slope) {
        return (double)point[1] - slope * (double)point[0];
    }

    private double slope(int[][] edge) {
        return (double)(edge[1][1] - edge[0][1]) / (double)(edge[1][0] - edge[0][0]);
    }

    public LinkedList<int[]> getNodes() {
        return this.nodes;
    }

    public LinkedList<int[][]> getEdges() {
        return this.edges;
    }

    public void setGraph(LinkedList<int[]> newNodes, LinkedList<int[]> newEdges) {
        this.nodes = newNodes;
        this.edges.clear();

        for (int[] edge : newEdges) {
            this.edges.add(new int[][]{this.nodes.get(edge[0]), this.nodes.get(edge[1])});
        }

        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Objects.equals(modes.getSelectedItem(), "EDGES") && Objects.equals(functions.getSelectedItem(), "ADD") && this.node != null && this.tempPoints != null) {
            g.drawLine(this.node[0], this.node[1], this.tempPoints[0], this.tempPoints[1]);
        }

        for (int[] xy : this.nodes) {
            g.fillOval(xy[0] - NODE_RADIUS, xy[1] - NODE_RADIUS, 2*NODE_RADIUS, 2*NODE_RADIUS);
        }

        for(int i = 0; i < this.edges.size(); ++i) {
            int[][] node = this.edges.get(i);
            if (this.nodes.contains(node[0]) && this.nodes.contains(node[1])) {
                g.drawLine(node[0][0], node[0][1], node[1][0], node[1][1]);
            } else {
                this.edges.remove(node);
                --i;
            }
        }

    }
}
