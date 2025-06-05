import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Notepad extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem newFile, openFile, saveFile, saveAsFile, exit;
    private JMenuItem cut, copy, paste, selectAll, find;
    private JMenuItem about;
    private JScrollPane scrollPane;
    
    private File currentFile = null;
    private boolean isModified = false;
    
    public Notepad() {
        initializeComponents();
        setupMenus();
        setupLayout();
        setupEventHandlers();
        
        setTitle("记事本 - 新建文档");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // 创建文本区域
        textArea = new JTextArea();
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // 创建滚动面板
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 创建菜单栏
        menuBar = new JMenuBar();
        
        // 文件菜单
        fileMenu = new JMenu("文件(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        newFile = new JMenuItem("新建(N)");
        newFile.setMnemonic(KeyEvent.VK_N);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        
        openFile = new JMenuItem("打开(O)");
        openFile.setMnemonic(KeyEvent.VK_O);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        
        saveFile = new JMenuItem("保存(S)");
        saveFile.setMnemonic(KeyEvent.VK_S);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        
        saveAsFile = new JMenuItem("另存为(A)");
        saveAsFile.setMnemonic(KeyEvent.VK_A);
        
        exit = new JMenuItem("退出(X)");
        exit.setMnemonic(KeyEvent.VK_X);
        
        // 编辑菜单
        editMenu = new JMenu("编辑(E)");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        cut = new JMenuItem("剪切(T)");
        cut.setMnemonic(KeyEvent.VK_T);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        
        copy = new JMenuItem("复制(C)");
        copy.setMnemonic(KeyEvent.VK_C);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        
        paste = new JMenuItem("粘贴(P)");
        paste.setMnemonic(KeyEvent.VK_P);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        
        selectAll = new JMenuItem("全选(A)");
        selectAll.setMnemonic(KeyEvent.VK_A);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        
        find = new JMenuItem("查找(F)");
        find.setMnemonic(KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        
        // 帮助菜单
        helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        about = new JMenuItem("关于");
    }
    
    private void setupMenus() {
        // 添加菜单项到文件菜单
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        
        // 添加菜单项到编辑菜单
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);
        editMenu.addSeparator();
        editMenu.add(find);
        
        // 添加菜单项到帮助菜单
        helpMenu.add(about);
        
        // 添加菜单到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        // 为菜单项添加事件监听器
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        saveAsFile.addActionListener(this);
        exit.addActionListener(this);
        
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        find.addActionListener(this);
        
        about.addActionListener(this);
        
        // 监听文本变化
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
        });
        
        // 监听窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == newFile) {
            newDocument();
        } else if (source == openFile) {
            openDocument();
        } else if (source == saveFile) {
            saveDocument();
        } else if (source == saveAsFile) {
            saveAsDocument();
        } else if (source == exit) {
            exitApplication();
        } else if (source == cut) {
            textArea.cut();
        } else if (source == copy) {
            textArea.copy();
        } else if (source == paste) {
            textArea.paste();
        } else if (source == selectAll) {
            textArea.selectAll();
        } else if (source == find) {
            showFindDialog();
        } else if (source == about) {
            showAboutDialog();
        }
    }
    
    private void newDocument() {
        if (checkSaveChanges()) {
            textArea.setText("");
            currentFile = null;
            setModified(false);
            setTitle("记事本 - 新建文档");
        }
    }
    
    private void openDocument() {
        if (checkSaveChanges()) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                    
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    reader.close();
                    
                    textArea.setText(content.toString());
                    currentFile = file;
                    setModified(false);
                    setTitle("记事本 - " + file.getName());
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "无法打开文件: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void saveDocument() {
        if (currentFile == null) {
            saveAsDocument();
        } else {
            saveToFile(currentFile);
        }
    }
    
    private void saveAsDocument() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
        
        if (currentFile != null) {
            fileChooser.setSelectedFile(currentFile);
        }
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }
            saveToFile(file);
        }
    }
    
    private void saveToFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(textArea.getText());
            writer.close();
            
            currentFile = file;
            setModified(false);
            setTitle("记事本 - " + file.getName());
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "无法保存文件: " + ex.getMessage(),
                "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean checkSaveChanges() {
        if (isModified) {
            int option = JOptionPane.showConfirmDialog(this,
                "文档已修改，是否保存更改？", "保存确认",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                saveDocument();
                return !isModified; // 如果保存失败，isModified仍为true
            } else if (option == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return false; // 取消操作
            }
        }
        return true;
    }
    
    private void exitApplication() {
        if (checkSaveChanges()) {
            System.exit(0);
        }
    }
    
    private void setModified(boolean modified) {
        isModified = modified;
        String title = getTitle();
        if (modified && !title.startsWith("*")) {
            setTitle("*" + title);
        } else if (!modified && title.startsWith("*")) {
            setTitle(title.substring(1));
        }
    }
    
    private void showFindDialog() {
        String searchText = JOptionPane.showInputDialog(this, "查找内容:", "查找", JOptionPane.PLAIN_MESSAGE);
        if (searchText != null && !searchText.isEmpty()) {
            String content = textArea.getText();
            int index = content.indexOf(searchText);
            
            if (index >= 0) {
                textArea.setCaretPosition(index);
                textArea.select(index, index + searchText.length());
            } else {
                JOptionPane.showMessageDialog(this, "未找到指定内容", "查找结果", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "简易记事本 v1.0\n\n" +
            "基于Java Swing开发\n" +
            "支持基本的文本编辑功能\n\n" +
            "作者: CST-Cat",
            "关于", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 在事件调度线程中创建GUI
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}