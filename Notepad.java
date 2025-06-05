import javax.swing.*; // 导入Swing组件包
import javax.swing.filechooser.FileNameExtensionFilter; // 导入文件选择器过滤器
import java.awt.*; // 导入AWT界面包
import java.awt.event.*; // 导入AWT事件包
import java.io.*; // 导入IO包
import java.nio.charset.StandardCharsets; // 导入字符集

/**
 * 简易记事本主类，继承自JFrame，实现ActionListener接口。
 * 提供基本的文本编辑功能，包括新建、打开、保存、另存为、剪切、复制、粘贴、查找、全选、关于等。
 * 使用Java Swing进行界面开发，支持窗口关闭事件和文档修改状态管理。
 */
public class Notepad extends JFrame implements ActionListener {
    // 文本编辑区
    private JTextArea textArea;
    // 菜单栏及各菜单
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, helpMenu;
    // 文件菜单项
    private JMenuItem newFile, openFile, saveFile, saveAsFile, exit;
    // 编辑菜单项
    private JMenuItem cut, copy, paste, selectAll, find;
    // 帮助菜单项
    private JMenuItem about;
    // 滚动面板
    private JScrollPane scrollPane;
    
    // 当前打开的文件对象
    private File currentFile = null;
    // 文档是否被修改的标志
    private boolean isModified = false;
    
    /**
     * 构造方法，初始化界面和各组件
     */
    public Notepad() {
        initializeComponents(); // 初始化组件
        setupMenus(); // 设置菜单
        setupLayout(); // 设置布局
        setupEventHandlers(); // 设置事件处理
        
        setTitle("记事本 - 新建文档"); // 设置窗口标题
        setSize(800, 600); // 设置窗口大小
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 禁止直接关闭，需处理保存
        setLocationRelativeTo(null); // 居中显示
        setVisible(true); // 显示窗口
    }
    
    /**
     * 初始化文本区、菜单栏、菜单项等界面组件
     */
    private void initializeComponents() {
        // 创建文本区域，设置字体和自动换行
        textArea = new JTextArea();
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 单词边界换行
        
        // 创建带滚动条的面板
        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 创建菜单栏和各菜单
        menuBar = new JMenuBar();
        
        // 文件菜单及其菜单项
        fileMenu = new JMenu("文件(F)");
        fileMenu.setMnemonic(KeyEvent.VK_F); // 快捷键Alt+F
        
        newFile = new JMenuItem("新建(N)");
        newFile.setMnemonic(KeyEvent.VK_N);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); // Ctrl+N
        
        openFile = new JMenuItem("打开(O)");
        openFile.setMnemonic(KeyEvent.VK_O);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)); // Ctrl+O
        
        saveFile = new JMenuItem("保存(S)");
        saveFile.setMnemonic(KeyEvent.VK_S);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)); // Ctrl+S
        
        saveAsFile = new JMenuItem("另存为(A)");
        saveAsFile.setMnemonic(KeyEvent.VK_A);
        
        exit = new JMenuItem("退出(X)");
        exit.setMnemonic(KeyEvent.VK_X);
        
        // 编辑菜单及其菜单项
        editMenu = new JMenu("编辑(E)");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        cut = new JMenuItem("剪切(T)");
        cut.setMnemonic(KeyEvent.VK_T);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)); // Ctrl+X
        
        copy = new JMenuItem("复制(C)");
        copy.setMnemonic(KeyEvent.VK_C);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)); // Ctrl+C
        
        paste = new JMenuItem("粘贴(P)");
        paste.setMnemonic(KeyEvent.VK_P);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)); // Ctrl+V
        
        selectAll = new JMenuItem("全选(A)");
        selectAll.setMnemonic(KeyEvent.VK_A);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK)); // Ctrl+A
        
        find = new JMenuItem("查找(F)");
        find.setMnemonic(KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)); // Ctrl+F
        
        // 帮助菜单及其菜单项
        helpMenu = new JMenu("帮助(H)");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        about = new JMenuItem("关于");
    }
    
    /**
     * 将菜单项添加到对应菜单，并将菜单添加到菜单栏
     */
    private void setupMenus() {
        // 文件菜单项
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator(); // 分隔线
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        
        // 编辑菜单项
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);
        editMenu.addSeparator();
        editMenu.add(find);
        
        // 帮助菜单项
        helpMenu.add(about);
        
        // 菜单加入菜单栏
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar); // 设置菜单栏
    }
    
    /**
     * 设置主窗口布局，将滚动面板添加到中心
     */
    private void setupLayout() {
        setLayout(new BorderLayout()); // 边界布局
        add(scrollPane, BorderLayout.CENTER); // 滚动面板居中
    }
    
    /**
     * 为各菜单项和窗口添加事件监听器
     */
    private void setupEventHandlers() {
        // 文件菜单事件
        newFile.addActionListener(this);
        openFile.addActionListener(this);
        saveFile.addActionListener(this);
        saveAsFile.addActionListener(this);
        exit.addActionListener(this);
        
        // 编辑菜单事件
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        selectAll.addActionListener(this);
        find.addActionListener(this);
        
        // 帮助菜单事件
        about.addActionListener(this);
        
        // 文本区内容变化监听，设置文档修改状态
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { setModified(true); }
        });
        
        // 窗口关闭事件，提示保存
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
        Object source = e.getSource(); // 获取事件源
        
        if (source == newFile) {
            newDocument(); // 新建
        } else if (source == openFile) {
            openDocument(); // 打开
        } else if (source == saveFile) {
            saveDocument(); // 保存
        } else if (source == saveAsFile) {
            saveAsDocument(); // 另存为
        } else if (source == exit) {
            exitApplication(); // 退出
        } else if (source == cut) {
            textArea.cut(); // 剪切
        } else if (source == copy) {
            textArea.copy(); // 复制
        } else if (source == paste) {
            textArea.paste(); // 粘贴
        } else if (source == selectAll) {
            textArea.selectAll(); // 全选
        } else if (source == find) {
            showFindDialog(); // 查找
        } else if (source == about) {
            showAboutDialog(); // 关于
        }
    }
    
    /**
     * 新建文档，若有修改则提示保存
     */
    private void newDocument() {
        if (checkSaveChanges()) { // 检查是否需要保存
            textArea.setText(""); // 清空文本
            currentFile = null; // 当前文件置空
            setModified(false); // 修改状态为未修改
            setTitle("记事本 - 新建文档"); // 标题
        }
    }
    
    /**
     * 打开文档，选择文件并读取内容到文本区
     */
    private void openDocument() {
        if (checkSaveChanges()) { // 检查是否需要保存
            JFileChooser fileChooser = new JFileChooser(); // 文件选择器
            fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt")); // 只显示txt
            
            int result = fileChooser.showOpenDialog(this); // 打开对话框
            if (result == JFileChooser.APPROVE_OPTION) { // 用户选择了文件
                File file = fileChooser.getSelectedFile(); // 获取文件
                try {
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); // 以UTF-8读取
                    
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n"); // 读取每一行
                    }
                    reader.close();
                    
                    textArea.setText(content.toString()); // 设置文本区内容
                    currentFile = file; // 记录当前文件
                    setModified(false); // 修改状态为未修改
                    setTitle("记事本 - " + file.getName()); // 更新标题
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "无法打开文件: " + ex.getMessage(),
                        "错误", JOptionPane.ERROR_MESSAGE); // 打开失败提示
                }
            }
        }
    }
    
    /**
     * 保存当前文档，若未指定文件则调用另存为
     */
    private void saveDocument() {
        if (currentFile == null) {
            saveAsDocument(); // 未指定文件则另存为
        } else {
            saveToFile(currentFile); // 保存到当前文件
        }
    }
    
    /**
     * 另存为功能，弹出文件选择框保存为新文件
     */
    private void saveAsDocument() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
        
        if (currentFile != null) {
            fileChooser.setSelectedFile(currentFile); // 默认文件名
        }
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt"); // 自动补全扩展名
            }
            saveToFile(file); // 保存
        }
    }
    
    /**
     * 将文本区内容保存到指定文件
     * @param file 目标文件
     */
    private void saveToFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)); // 以UTF-8写入
            writer.write(textArea.getText()); // 写入内容
            writer.close();
            
            currentFile = file; // 更新当前文件
            setModified(false); // 修改状态为未修改
            setTitle("记事本 - " + file.getName()); // 更新标题
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "无法保存文件: " + ex.getMessage(),
                "错误", JOptionPane.ERROR_MESSAGE); // 保存失败提示
        }
    }
    
    /**
     * 检查文档是否被修改，若有修改则提示保存
     * @return 是否可以继续后续操作
     */
    private boolean checkSaveChanges() {
        if (isModified) { // 如果已修改
            int option = JOptionPane.showConfirmDialog(this,
                "文档已修改，是否保存更改？", "保存确认",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                saveDocument(); // 选择保存
                return !isModified; // 如果保存失败，isModified仍为true
            } else if (option == JOptionPane.NO_OPTION) {
                return true; // 不保存
            } else {
                return false; // 取消操作
            }
        }
        return true; // 未修改直接返回
    }
    
    /**
     * 退出应用，检查是否需要保存
     */
    private void exitApplication() {
        if (checkSaveChanges()) { // 检查是否需要保存
            System.exit(0); // 退出程序
        }
    }
    
    /**
     * 设置文档修改状态，并在标题栏显示星号
     * @param modified 是否被修改
     */
    private void setModified(boolean modified) {
        isModified = modified; // 设置状态
        String title = getTitle();
        if (modified && !title.startsWith("*")) {
            setTitle("*" + title); // 添加星号
        } else if (!modified && title.startsWith("*")) {
            setTitle(title.substring(1)); // 去掉星号
        }
    }
    
    /**
     * 弹出查找对话框并高亮查找到的内容
     */
    private void showFindDialog() {
        String searchText = JOptionPane.showInputDialog(this, "查找内容:", "查找", JOptionPane.PLAIN_MESSAGE); // 输入查找内容
        if (searchText != null && !searchText.isEmpty()) {
            String content = textArea.getText();
            int index = content.indexOf(searchText); // 查找
            
            if (index >= 0) {
                textArea.setCaretPosition(index); // 移动光标
                textArea.select(index, index + searchText.length()); // 选中内容
            } else {
                JOptionPane.showMessageDialog(this, "未找到指定内容", "查找结果", JOptionPane.INFORMATION_MESSAGE); // 未找到提示
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
            "关于", JOptionPane.INFORMATION_MESSAGE); // 关于信息
    }
    
    /**
     * 主方法，设置系统外观并启动应用
     */
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