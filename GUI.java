import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;


public class GUI extends JPanel {
    private static final long serialVersionUID = 1L;
    private ArrayList<JTextField> edgesTF;
    JTabbedPane tabbedPane;
    JPanel adjListPanel;

    public GUI() {
        super(new GridLayout(1, 1));
        this.edgesTF = new ArrayList<>();

        tabbedPane = new JTabbedPane();
        
        JComponent setup = createSetupPanel();
        tabbedPane.addTab("Setup", setup);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent visual = makeTextPanel("Panel #2");
        tabbedPane.addTab("Visual", visual);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.setEnabledAt(1, false);
        
        adjListPanel = new JPanel(new BorderLayout());
        tabbedPane.addTab("Adjacency List", adjListPanel);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        tabbedPane.setEnabledAt(2, false);
        
        JComponent adjMatrix = makeTextPanel( "Panel #4");
        adjMatrix.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Adjacency Matrix", adjMatrix);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        tabbedPane.setEnabledAt(3, false);
        
        add(tabbedPane);
        
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private JPanel createSetupPanel(){
        JPanel panel = new JPanel(false);
        JPanel form = new JPanel(new GridLayout(2,2));
        JPanel edges = new JPanel(false);
        edges.setAutoscrolls(true);
        JScrollPane edgesMain = new JScrollPane(edges); 
        edgesMain.setPreferredSize(new Dimension(200, 200));


        Integer[] numbers = new Integer[100];
        for(int i = 0; i < 100; ++i){
            numbers[i] = i+1;
        }
        JLabel nVerticesLabel = new JLabel("Select number of vertices");
        JComboBox nVertices = new JComboBox<>(numbers);
        JLabel nEdgesLabel = new JLabel("Select number of edges");
        JComboBox nEdges = new JComboBox<>(numbers);
        JButton generate = new JButton("Generate!");
        generate.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event) {
                ArrayList<Edge> edgesArr = new ArrayList<>();
                // Loop over inputs 
                for(int i = 0; i < edgesTF.size(); i+=2){
                    JTextField u = edgesTF.get(i); 
                    JTextField v = edgesTF.get(i+1); 
                    Edge e = new Edge(Integer.valueOf(u.getText()), Integer.valueOf(v.getText()));
                    edgesArr.add(e);
                }
                generateGraph((Integer) nVertices.getSelectedItem(), edgesArr);
            }
        });
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(generate);

        nEdges.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                edges.removeAll();
                edges.add(createEdgesInput((Integer) nEdges.getSelectedItem()));
                edges.revalidate();
                edges.repaint();
            }
        });

        form.add(nVerticesLabel);
        form.add(nVertices);
        form.add(nEdgesLabel);
        form.add(nEdges);

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(form);
        panel.add(edgesMain);
        panel.add(bottomPanel);
        return panel;
    }

    private JPanel createEdgesInput(int nEdges){
        edgesTF.clear();
        JPanel panel = new JPanel(new GridLayout(nEdges+1, 2));
        panel.add(new JLabel("From"));
        panel.add(new JLabel("To"));
        Dimension size = new Dimension(50, 20);
        for(int i = 0; i < nEdges; ++i){
            JTextField u = new JTextField();
            JTextField v = new JTextField();
            u.setPreferredSize(size);
            v.setPreferredSize(size);
            panel.add(u);
            panel.add(v);
            edgesTF.add(u);
            edgesTF.add(v);
        }
        return panel;
    }

    private void generateGraph(Integer nVertices, ArrayList<Edge> edges){
        // Undirected for now
        IGraphBuilder graph = new UndirectedGraphBuilder(nVertices, edges); 
        // Adjacency list
        graph.buildAdjacencyList();
        ArrayList<Integer>[] adjList = graph.getAdjacencyList();
        tabbedPane.setEnabledAt(2, true);
        adjListPanel.removeAll();
        adjListPanel.add(createAdjacencyListView(adjList));
        adjListPanel.repaint();
            
    }

    private JPanel createAdjacencyListView(ArrayList<Integer>[] list){
        JPanel panel = new JPanel(new GridLayout(list.length, 2));
        for(int i = 0; i < list.length; ++i){
            JLabel src = new JLabel(String.valueOf(i));
            JLabel dst = new JLabel(sexyArrayList(list[i]));
            panel.add(src);
            panel.add(dst);
        }

        return panel;
    }

    private <T> String sexyArrayList(ArrayList<T> arr){
        String out = "[";
        for(int i = 0; i < arr.size(); ++i){
            out = out + arr.get(i) + ",";
        }
        out = out + "]";
        return out;
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Graph helper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(new GUI(), BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
        createAndShowGUI();
            }
        });
    }
}
