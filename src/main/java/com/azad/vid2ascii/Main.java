package com.azad.vid2ascii;

import com.azad.vid2ascii.utils.ASCII;
import net.coobird.thumbnailator.Thumbnails;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main extends JPanel {

    private JTextArea textArea;
    private AbstractAction action;
    private File file;

    public Main() {
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(
                BorderFactory.createLineBorder(
                        Color.DARK_GRAY, 5));
        this.setPreferredSize(new Dimension(1400, 800));

        textArea = new JTextArea();
       // textArea.setForeground(Color.BLACK);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 5));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);

        action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable runner = new Runnable() {
                    public void run() {
                        try {
                            FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
                            Picture picture = null;
                            while (null != (picture = grab.getNativeFrame())) {
                                try {
                                    TimeUnit.MILLISECONDS.sleep(23);
                                  //  Picture scaledPic = new Picture()
                                    onClick(AWTUtil.toBufferedImage(picture));
                                } catch (Exception e){

                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                };
                new Thread(runner, "dummy thread").start();
            }
        };

    }

    private void onClick(BufferedImage bufferedImageArr) {
        BufferedImage scaledImg = resizeImg(bufferedImageArr, 300, 150);
        String ascii = new ASCII().convert(scaledImg);
        textArea.setText(ascii);
    }

    private BufferedImage resizeImg(BufferedImage img, int newW, int newH) {
        BufferedImage scaledImg = null;
        try {
            scaledImg = Thumbnails.of(img).size(newW, newH).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImg;
    }

    private void startUI() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Video-mp4", "mp4"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                file = fileChooser.getSelectedFile();
            } catch (Exception e){

            }
        }
        action.actionPerformed(new ActionEvent(this, 1001, "DUMMY_ACTION"));
    }

    /*
    private void showFrame() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Video-mp4", "mp4"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
                Picture picture = null;
                while (null != (picture = grab.getNativeFrame())) {
                    Thread.sleep(50);
                    //    BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
                    //    String ascii = new ASCII().convert(bufferedImage);
                    //   textArea.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
                    //   textArea.replaceRange(ascii, 0, ascii.length()-1);
                    //    System.out.println(ascii);
                    textArea.setSize(960, 720);
                    // textArea.replaceRange();
                    textArea.append("HELLO WORLD /n");
                    this.removeAll();
                    this.revalidate();
                    this.repaint();
                  //  textArea.setCaretPosition(textArea.getDocument().getLength());
                  //  scrollPane.validate();
                }
            } catch (Exception e) {

            }
        }
    }
    */
    public static void main(String[] args){
        Main main = new Main();
        final JFrame frame = new JFrame(Main.class.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(main);
        frame.pack();
        frame.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                main.startUI();
            }
        });
    };
}
