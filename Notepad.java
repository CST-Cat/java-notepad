import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 简易记事本主类，继承自JFrame，实现ActionListener接口。
 * 提供基本的文本编辑功能，包括新建、打开、保存、另存为、剪切、复制、粘贴、查找、全选、关于等。
 * 使用Java Swing进行界面开发，支持窗口关闭事件和文档修改状态管理。
 */
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

    /**
     * 构造方法，初始化界面和各组件
     */
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

    /**
     * 初始化文本区、菜单栏、菜单项等界面组件
     */
    private void initializeComponents() {
        textArea = new JTextArea();
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        menuBar = new JMenuBar();

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

        helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        about = new JMenuItem("关于");
    }

    /**
     * 将菜单项添加到对应菜单，并将菜单添加到菜单栏
     */
    private void setupMenus() {
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);
        editMenu.addSeparator();
        editMenu.add(find);

        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * 设置主窗口布局，将滚动面板添加到中心
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 为各菜单项和窗口添加事件监听器
     */
    private void setupEventHandlers() {
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

        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }

    /**
     * 菜单项事件统一处理方法，根据事件源分发到对应功能
     */
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

    /**
     * 新建文档，若有修改则提示保存
     */
    private void newDocument() {
        if (checkSaveChanges()) {
            textArea.setText("");
            currentFile = null;
            setModified(false);
            setTitle("记事本 - 新建文档");
        }
    }

    /**
     * 打开文档，选择文件并读取内容到文本区
     */
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

    /**
     * 保存当前文档，若未指定文件则调用另存为
     */
    private void saveDocument() {
        if (currentFile == null) {
            saveAsDocument();
        } else {
            saveToFile(currentFile);
        }
    }

    /**
     * 另存为功能，弹出文件选择框保存为新文件
     */
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

    /**
     * 将文本区内容保存到指定文件
     * @param file 目标文件
     */
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

    /**
     * 检查文档是否被修改，若有修改则提示保存
     * @return 是否可以继续后续操作
     */
    private boolean checkSaveChanges() {
        if (isModified) {
            int option = JOptionPane.showConfirmDialog(this,
                "文档已修改，是否保存更改？", "保存确认",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                saveDocument();
                return !isModified;
            } else if (option == JOptionPane.NO_OPTION) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 退出应用，检查是否需要保存
     */
    private void exitApplication() {
        if (checkSaveChanges()) {
            System.exit(0);
        }
    }

    /**
     * 设置文档修改状态，并在标题栏显示星号
     * @param modified 是否被修改
     */
    private void setModified(boolean modified) {
        isModified = modified;
        String title = getTitle();
        if (modified && !title.startsWith("*")) {
            setTitle("*" + title);
        } else if (!modified && title.startsWith("*")) {
            setTitle(title.substring(1));
        }
    }

    /**
     * 弹出查找对话框并高亮查找到的内容
     */
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

    /**
     * 显示关于对话框
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "简易记事本 v1.0\n\n" +
            "基于Java Swing开发\n" +
            "支持基本的文本编辑功能\n\n" +
            "作者: CST-Cat",
            "关于", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 主方法，设置系统外观并启动应用
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}