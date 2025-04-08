package view;

import controller.CategoryController;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryPanel extends JPanel {
    private CategoryController categoryController;
    
    private JTextField txtName;
    private JTextField txtDescription;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    
    private JTable tblCategories;
    private DefaultTableModel tableModel;
    
    private int selectedCategoryId = -1;

    public CategoryPanel(CategoryController categoryController) {
        this.categoryController = categoryController;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Cadastro de Categorias"));
        
        formPanel.add(new JLabel("Nome:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("Descrição:"));
        txtDescription = new JTextField();
        formPanel.add(txtDescription);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSave = new JButton("Salvar");
        btnUpdate = new JButton("Atualizar");
        btnDelete = new JButton("Excluir");
        btnClear = new JButton("Limpar");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        add(formPanel, BorderLayout.NORTH);
        
        // Tabela de categorias
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta na tabela
            }
        };
        
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Descrição");
        
        tblCategories = new JTable(tableModel);
        tblCategories.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblCategories.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblCategories.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tblCategories);
        add(scrollPane, BorderLayout.CENTER);
        
        // Adiciona os eventos
        setupEvents();
        
        // Carrega as categorias iniciais
        refreshTable();
    }
    
    private void setupEvents() {
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText().trim();
                String description = txtDescription.getText().trim();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um nome para a categoria!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (categoryController.addCategory(name, description)) {
                    JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                    updateOtherPanels();
                } else {
                    JOptionPane.showMessageDialog(this, "Já existe uma categoria com este nome!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar categoria: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnUpdate.addActionListener(e -> {
            try {
                if (selectedCategoryId == -1) {
                    JOptionPane.showMessageDialog(this, "Selecione uma categoria para atualizar!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String name = txtName.getText().trim();
                String description = txtDescription.getText().trim();
                
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um nome para a categoria!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (categoryController.updateCategory(selectedCategoryId, name, description)) {
                    JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                    updateOtherPanels();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível atualizar a categoria!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar categoria: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnDelete.addActionListener(e -> {
            try {
                if (selectedCategoryId == -1) {
                    JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int option = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir esta categoria?", 
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    if (categoryController.deleteCategory(selectedCategoryId)) {
                        JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!", 
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        refreshTable();
                        updateOtherPanels();
                    } else {
                        JOptionPane.showMessageDialog(this, "Não foi possível excluir a categoria!", 
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir categoria: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnClear.addActionListener(e -> {
            clearFields();
        });
        
        tblCategories.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblCategories.getSelectedRow();
                if (row != -1) {
                    selectedCategoryId = (int) tableModel.getValueAt(row, 0);
                    txtName.setText((String) tableModel.getValueAt(row, 1));
                    txtDescription.setText((String) tableModel.getValueAt(row, 2));
                    
                    btnUpdate.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnSave.setEnabled(false);
                }
            }
        });
    }
    
    private void clearFields() {
        txtName.setText("");
        txtDescription.setText("");
        selectedCategoryId = -1;
        tblCategories.clearSelection();
        
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
    }
    
    private void refreshTable() {
        // Limpa a tabela
        tableModel.setRowCount(0);
        
        // Obtém todas as categorias e adiciona à tabela
        List<Category> categories = categoryController.getAllCategories();
        
        for (Category category : categories) {
            tableModel.addRow(new Object[]{
                category.getId(),
                category.getName(),
                category.getDescription()
            });
        }
    }
    
    private void updateOtherPanels() {
        // Atualiza outras abas quando houver alterações nas categorias
        if (getParent() instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane) getParent();
            
            Component comp = pane.getComponentAt(1); // Transações
            if (comp instanceof TransactionPanel) {
                ((TransactionPanel) comp).refresh();
            }
            
            comp = pane.getComponentAt(2); // Histórico
            if (comp instanceof HistoryPanel) {
                ((HistoryPanel) comp).refreshTable();
            }
        }
    }
}
