package com.ibik.pbo.applications;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import com.ibik.pbo.connections.Tugas;
import com.ibik.pbo.connections.TugasDao;
import com.ibik.pbo.connections.TugasUsers;
import com.ibik.pbo.connections.TugasUsersDao;

public class UserPage extends JFrame {

    private JPanel contentPane;
    private JTable siswaTable;
    private JTable adminTable;
    private DefaultTableModel siswaTableModel;
    private DefaultTableModel adminTableModel;
    private JTextField namaSiswaField;
    private JButton uploadFileButton;
    private JButton saveButton, updateButton, deleteButton;
    private File selectedFile;

    public UserPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("User Page");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        titleLabel.setBounds(420, 10, 200, 30);
        contentPane.add(titleLabel);

        JPanel siswaPanel = new JPanel();
        siswaPanel.setLayout(null);
        siswaPanel.setBounds(20, 50, 460, 500);
        contentPane.add(siswaPanel);

        JLabel siswaLabel = new JLabel("Pengumpulan Tugas Siswa");
        siswaLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        siswaLabel.setBounds(10, 10, 200, 20);
        siswaPanel.add(siswaLabel);

        String[] siswaColumns = {"ID", "Nama Siswa", "File"};
        siswaTableModel = new DefaultTableModel(siswaColumns, 0);
        siswaTable = new JTable(siswaTableModel);
        JScrollPane siswaScrollPane = new JScrollPane(siswaTable);
        siswaScrollPane.setBounds(10, 40, 440, 200);
        siswaPanel.add(siswaScrollPane);

        JLabel namaSiswaLabel = new JLabel("Nama Siswa:");
        namaSiswaLabel.setBounds(10, 260, 100, 20);
        siswaPanel.add(namaSiswaLabel);

        namaSiswaField = new JTextField();
        namaSiswaField.setBounds(120, 260, 200, 20);
        siswaPanel.add(namaSiswaField);

        uploadFileButton = new JButton("Upload File");
        uploadFileButton.setBounds(10, 300, 150, 30);
        siswaPanel.add(uploadFileButton);

        saveButton = new JButton("Save");
        saveButton.setBounds(10, 350, 100, 30);
        siswaPanel.add(saveButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(120, 350, 100, 30);
        siswaPanel.add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(230, 350, 100, 30);
        siswaPanel.add(deleteButton);

        JButton downloadFileButton = new JButton("Download");
        downloadFileButton.setBounds(340, 350, 100, 30);
        siswaPanel.add(downloadFileButton);

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(null);
        adminPanel.setBounds(500, 50, 460, 500);
        contentPane.add(adminPanel);

        JLabel adminLabel = new JLabel("List Tugas");
        adminLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        adminLabel.setBounds(10, 10, 200, 20);
        adminPanel.add(adminLabel);

        String[] adminColumns = {"Judul", "Deskripsi", "File"};
        adminTableModel = new DefaultTableModel(adminColumns, 0);
        adminTable = new JTable(adminTableModel);
        JScrollPane adminScrollPane = new JScrollPane(adminTable);
        adminScrollPane.setBounds(10, 40, 440, 450);
        adminPanel.add(adminScrollPane);
        
        downloadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = adminTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Pilih baris yang berisi file untuk diunduh!");
                    return;
                }

                try {
                    String judul = (String) adminTableModel.getValueAt(selectedRow, 0);

                 
                    TugasDao tugasDao = new TugasDao();
                    Tugas tugas = tugasDao.findByJudul(judul);

                    if (tugas != null && tugas.getFile_tugas() != null) {
                       
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Pilih lokasi untuk menyimpan file");
                        fileChooser.setSelectedFile(new File(judul + ".pdf")); 
                        int userSelection = fileChooser.showSaveDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            File fileToSave = fileChooser.getSelectedFile();
                            Files.write(fileToSave.toPath(), tugas.getFile_tugas());
                            JOptionPane.showMessageDialog(null, "File berhasil diunduh ke " + fileToSave.getAbsolutePath());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "File tidak tersedia untuk tugas ini!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error saat mengunduh file: " + ex.getMessage());
                }
            }
        });


        uploadFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    JOptionPane.showMessageDialog(null, "File selected: " + selectedFile.getName());
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TugasUsersDao tugasUsersDao = new TugasUsersDao();
                    TugasUsers tugasUser = new TugasUsers();
                    tugasUser.setNama_siswa(namaSiswaField.getText());
                    if (selectedFile != null) {
                        tugasUser.setFile_siswa(Files.readAllBytes(selectedFile.toPath()));
                    }
                    tugasUsersDao.save(tugasUser);
                    JOptionPane.showMessageDialog(null, "Tugas siswa berhasil disimpan!");
                    loadSiswaData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error saving siswa data: " + ex.getMessage());
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = siswaTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Pilih baris untuk diupdate!");
                        return;
                    }

                    int idTugasSiswa = (int) siswaTableModel.getValueAt(selectedRow, 0);
                    TugasUsersDao tugasUsersDao = new TugasUsersDao();
                    TugasUsers tugasUser = new TugasUsers();
                    tugasUser.setId_tugas_siswa(idTugasSiswa);
                    tugasUser.setNama_siswa(namaSiswaField.getText());

                   
                    if (selectedFile != null) {
                        tugasUser.setFile_siswa(Files.readAllBytes(selectedFile.toPath()));
                    } else {
                       
                        TugasUsers existingTugasUser = tugasUsersDao.findById(idTugasSiswa);
                        if (existingTugasUser != null && existingTugasUser.getFile_siswa() != null) {
                            tugasUser.setFile_siswa(existingTugasUser.getFile_siswa());
                        }
                    }

                    tugasUsersDao.update(tugasUser);
                    JOptionPane.showMessageDialog(null, "Tugas siswa berhasil diperbarui!");
                    loadSiswaData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error updating siswa data: " + ex.getMessage());
                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int selectedRow = siswaTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "Pilih baris untuk dihapus!");
                        return;
                    }

                    int idTugasSiswa = (int) siswaTableModel.getValueAt(selectedRow, 0);
                    TugasUsersDao tugasUsersDao = new TugasUsersDao();
                    tugasUsersDao.deleteById(idTugasSiswa);
                    JOptionPane.showMessageDialog(null, "Tugas siswa berhasil dihapus!");
                    loadSiswaData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error deleting siswa data: " + ex.getMessage());
                }
            }
        });

        loadSiswaData();
        loadAdminData();
    }

    private void loadSiswaData() {
        try {
            siswaTableModel.setRowCount(0);
            TugasUsersDao tugasUsersDao = new TugasUsersDao();
            List<TugasUsers> tugasSiswaList = tugasUsersDao.findAll();

            for (TugasUsers tugasSiswa : tugasSiswaList) {
                siswaTableModel.addRow(new Object[]{
                        tugasSiswa.getId_tugas_siswa(),
                        tugasSiswa.getNama_siswa(),
                        tugasSiswa.getFile_siswa() != null ? "File tersedia" : "Tidak ada file"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading siswa data: " + ex.getMessage());
        }
    }

    private void loadAdminData() {
        try {
            adminTableModel.setRowCount(0); 
            TugasDao tugasDao = new TugasDao(); 
            for (Tugas tugas : tugasDao.findAll()) {
                adminTableModel.addRow(new Object[]{
                        tugas.getJudul(),
                        tugas.getDeskripsi(),
                        tugas.getFile_tugas() != null ? "File terupload" : "No file"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading admin data: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserPage frame = new UserPage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
