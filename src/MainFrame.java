import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.util.List;

public class MainFrame extends JFrame {

    private NoticeManager manager;
    private User user;

    private JTable noticeTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel userInfoLabel;
    private JButton refreshBtn;

    public MainFrame(NoticeManager manager, User user) {
        this.manager = manager;
        this.user = user;

        setTitle("University Notice System - " + user.getName());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(240, 245, 250));

        userInfoLabel = new JLabel("<html><b>사용자:</b> " + user.getName() + " | <b>관심 키워드:</b> "
                + user.getSubscribedKeywords() + "</html>");
        userInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        topPanel.add(userInfoLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);

        JButton settingsBtn = new JButton("설정");
        settingsBtn.addActionListener(e -> openSettingsDialog());
        btnPanel.add(settingsBtn);

        refreshBtn = new JButton("새로 고침 (수집)");
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        btnPanel.add(refreshBtn);

        topPanel.add(btnPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = { "번호", "날짜", "카테고리", "제목", "출처" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        noticeTable = new JTable(tableModel);
        noticeTable.setRowHeight(30);
        noticeTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 12));
        noticeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        noticeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = noticeTable.rowAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && row >= 0) {
                    openLink(row);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(noticeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel(" 시스템이 준비되었습니다.");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(150, 20));

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("F5"), "refresh");
        getRootPane().getActionMap().put("refresh", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                startCrawling(progressBar);
            }
        });

        refreshBtn.addActionListener(e -> startCrawling(progressBar));

        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        List<Notice> stored = manager.getStoredNotices();

        for (int i = 0; i < stored.size(); i++) {
            Notice n = stored.get(i);
            tableModel.addRow(new Object[] {
                    i + 1, n.getDate(), n.getCategory(), n.getTitle(), n.getSource()
            });
        }
        statusLabel.setText(" 총 " + stored.size() + "개의 공지가 로드되었습니다.");
    }

    private void startCrawling(JProgressBar progressBar) {
        statusLabel.setText(" 설정을 로드하고 최신 공지를 수집 중입니다...");
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        refreshBtn.setEnabled(false); // 수집 중에는 버튼 비활성화

        new Thread(() -> {
            try {
                User freshUser = UserPersistence.load();
                this.user = freshUser;

                int newCount = manager.run(user);

                SwingUtilities.invokeLater(() -> {
                    userInfoLabel.setText("<html><b>사용자:</b> " + user.getName() + " | <b>관심 키워드:</b> "
                            + user.getSubscribedKeywords() + "</html>");
                    updateTable();
                    progressBar.setVisible(false);
                    if (newCount > 0) {
                        JOptionPane.showMessageDialog(this, newCount + "개의 새로운 공지가 발견되었습니다!", "수집 완료",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "새로운 공지가 없습니다.", "수집 완료", JOptionPane.INFORMATION_MESSAGE);
                    }
                    refreshBtn.setEnabled(true); // 완료 후 다시 활성화
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(" 수집 중 오류 발생: " + e.getMessage());
                    progressBar.setVisible(false);
                    refreshBtn.setEnabled(true); // 에러 발생 시에도 다시 활성화
                });
            }
        }).start();
    }

    private void openLink(int row) {
        try {
            Notice n = manager.getStoredNotices().get(row);
            if (n.getContent() != null && n.getContent().startsWith("http")) {
                Desktop.getDesktop().browse(new URI(n.getContent()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "링크를 열 수 없습니다.");
        }
    }

    private void openSettingsDialog() {
        JDialog dialog = new JDialog(this, "사용자 설정", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("이름: "));
        JTextField nameField = new JTextField(user.getName(), 20);
        namePanel.add(nameField);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String kw : user.getSubscribedKeywords())
            listModel.addElement(kw);
        JList<String> keywordList = new JList<>(listModel);

        JPanel kwInputPanel = new JPanel(new BorderLayout());
        JTextField kwField = new JTextField();
        JButton addBtn = new JButton("추가");
        addBtn.addActionListener(e -> {
            String kw = kwField.getText().trim();
            if (!kw.isEmpty() && !listModel.contains(kw)) {
                listModel.addElement(kw);
                kwField.setText("");
            }
        });
        kwInputPanel.add(kwField, BorderLayout.CENTER);
        kwInputPanel.add(addBtn, BorderLayout.EAST);

        JButton removeBtn = new JButton("선택 삭제");
        removeBtn.addActionListener(e -> {
            int idx = keywordList.getSelectedIndex();
            if (idx >= 0)
                listModel.remove(idx);
        });

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createTitledBorder("관심 키워드"));
        centerPanel.add(kwInputPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(keywordList), BorderLayout.CENTER);
        centerPanel.add(removeBtn, BorderLayout.SOUTH);

        JButton saveBtn = new JButton("저장하기");
        saveBtn.addActionListener(e -> {
            user.setName(nameField.getText().trim());
            user.getSubscribedKeywords().clear();
            for (int i = 0; i < listModel.size(); i++) {
                user.addKeyword(listModel.get(i));
            }

            UserPersistence.save(user); // 저장 버튼을 누르면 실행되는 method. 실행 중 변경된 사용자 정보를 다시 파일로 저장

            manager.cleanupNotices(user);
            userInfoLabel.setText("<html><b>사용자:</b> " + user.getName() + " | <b>관심 키워드:</b> "
                    + user.getSubscribedKeywords() + "</html>");
            updateTable();

            dialog.dispose();
            JOptionPane.showMessageDialog(this, "설정이 저장되었습니다.");
        });

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(namePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(saveBtn, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
