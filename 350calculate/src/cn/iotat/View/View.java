package cn.iotat.View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.nio.channels.NonWritableChannelException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import cn.iotat.Constants.Constants;
import cn.iotat.SerialPort.SerialPort;
import cn.iotat.Utils.ShowUtils;
import gnu.io.PortInUseException;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Color;
@SuppressWarnings ("all")
public class View extends JFrame{

	public static JFrame frame;
	public static  JTextField towerheightField;
	public static  JTextField angleField;
	public static  JTextField correctField;
	public static  JTextField devField;
	public static  JTextField devdisField;
	public static  JTextField devdirectiontextField;
	public static  JTextField dipangleField;
	public static  JTextField dipangledisField;
	public static  JTextField dipangledOrenField;
	public static  JComboBox serialportchoosecomboBox=new JComboBox();
	public static  JComboBox baudcomboBox = new JComboBox();
	public static  JButton serialportbutton = new JButton("�򿪴���");
	// �����б�
	public static  List<String> mCommList = null;
	// ���ڶ���
     public static gnu.io.SerialPort mSerialport;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
		initView();
		initData();
		actionListener();
		
	}
	//��ʼ������
	private void initData() {
		try {
		mCommList = SerialPort.getPorts();

		// ����Ƿ��п��ô��ڣ��������ѡ����
		if (mCommList == null || mCommList.size() < 1) {
			ShowUtils.warningMessage("û����������Ч���ڣ�");
		} else {
			for (String s : mCommList) {
				serialportchoosecomboBox.addItem(s);
			}
		}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
       
	    baudcomboBox.addItem("9600");
		baudcomboBox.addItem("19200");
		baudcomboBox.addItem("38400");
		baudcomboBox.addItem("57600");
		baudcomboBox.addItem("115200");
	}
	private void actionListener() {
		// ����
		serialportchoosecomboBox.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				mCommList = SerialPort.getPorts();
				// ����Ƿ��п��ô��ڣ��������ѡ����
				if (mCommList == null || mCommList.size() < 1) {
					ShowUtils.warningMessage("û����������Ч���ڣ�");
				} else {
					int index = serialportchoosecomboBox.getSelectedIndex();
					serialportchoosecomboBox.removeAllItems();
					for (String s : mCommList) {
						serialportchoosecomboBox.addItem(s);
					}
					serialportchoosecomboBox.setSelectedIndex(index);
				}
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// NO OP
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// NO OP
			}
		});

		// ��|�رմ���
		serialportbutton.addActionListener(new ActionListener() {
           
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if ("�򿪴���".equals(serialportbutton.getText()) && SerialPort.theOnlyPort == null) {
					openSerialPort(e);
					Constants.m=Float.parseFloat(towerheightField.getText());
					Constants.x_east_offset_angle=Float.parseFloat(angleField.getText());
					Constants.adjust_deflection_offset_interval=Float.parseFloat(correctField.getText());
				} else {
					closeSerialPort(e);
				}
				SerialPort.addListener(SerialPort.theOnlyPort, new SerialPort.DataAvailableListener() {
			@Override
			public void dataAvailable() {
				SerialPort.readData(SerialPort.theOnlyPort);
			}
		});
			}
		});
		

	}
         //��ʼ������
	private void initView() {
		// �رճ���
		setDefaultCloseOperation(View.DISPOSE_ON_CLOSE);
		// ��ֹ�������
		setResizable(false);

		// ���ó��򴰿ھ�����ʾ
		Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
		getContentPane().setLayout(null);
		setTitle("�й���������ϵͳ");
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//������
		frame = new JFrame();
		frame.setBounds(100, 100, 1089, 847);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//��������
		JLabel titlelabel = new JLabel("\u4E2D\u56FD\u94C1\u5854\u6D4B\u8BD5\u7CFB\u7EDF");
		titlelabel.setFont(new Font("΢���ź�", Font.PLAIN, 38));
		titlelabel.setBounds(380, 23, 335, 47);
		frame.getContentPane().add(titlelabel);
		//��������
		JPanel setPanel = new JPanel();
		setPanel.setBorder(new TitledBorder(null, "\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setPanel.setToolTipText("");
		setPanel.setBounds(88, 91, 280, 381);
		frame.getContentPane().add(setPanel);
		setPanel.setLayout(null);
		
		JLabel towerheightLabel = new JLabel("\u5854\u9AD8M");
		towerheightLabel.setBounds(38, 214, 52, 18);
		setPanel.add(towerheightLabel);
		
		towerheightField = new JTextField();
		towerheightField.setText("30");
		towerheightField.setBounds(123, 211, 86, 24);
		setPanel.add(towerheightField);
		towerheightField.setColumns(10);
		
		JLabel angleLabel = new JLabel("\u4E0E\u6B63\u4E1C\u65B9\u5939\u89D2");
		angleLabel.setBounds(14, 272, 98, 33);
		setPanel.add(angleLabel);
		
		angleField = new JTextField();
		angleField.setText("0");
		angleField.setBounds(123, 276, 86, 24);
		setPanel.add(angleField);
		angleField.setColumns(10);
		
		JLabel correctLabel = new JLabel("\u5782\u5EA6\u77EB\u6B63\u95F4\u9694");
		correctLabel.setBounds(14, 337, 98, 18);
		setPanel.add(correctLabel);
		
		correctField = new JTextField();
		correctField.setText("0");
		correctField.setBounds(123, 334, 86, 24);
		setPanel.add(correctField);
		correctField.setColumns(10);
		
		
		serialportchoosecomboBox.setBounds(123, 41, 86, 24);
		setPanel.add(serialportchoosecomboBox);
		
		JLabel serialportchooseLabel = new JLabel("\u4E32\u53E3\u9009\u62E9");
		serialportchooseLabel.setBounds(18, 44, 72, 18);
		setPanel.add(serialportchooseLabel);
		
		JLabel baudLabel = new JLabel("\u6CE2\u7279\u7387\u8BBE\u7F6E");
		baudLabel.setBounds(10, 100, 80, 18);
		setPanel.add(baudLabel);
		
		
		serialportbutton.setBounds(66, 155, 113, 27);
		setPanel.add(serialportbutton);
		

		baudcomboBox.setBounds(123, 97, 86, 24);
		setPanel.add(baudcomboBox);
		
		JLabel label = new JLabel("\uFF08\u987A\u65F6\u9488\u4E3A\u6B63\uFF09");
		label.setBounds(14, 303, 105, 18);
		setPanel.add(label);
		//������ʾ����
		JPanel datepanel = new JPanel();
		datepanel.setBorder(new TitledBorder(null, "\u6570\u636E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		datepanel.setBounds(450, 91, 580, 378);
		frame.getContentPane().add(datepanel);
		datepanel.setLayout(null);
		
		JLabel devLabel = new JLabel("\u504F\u79FB\u89D2\u5EA6");
		devLabel.setFont(new Font("����", Font.PLAIN, 20));
		devLabel.setBounds(47, 41, 86, 41);
		datepanel.add(devLabel);
		
		devField = new JTextField();
		devField.setForeground(Color.RED);
		devField.setHorizontalAlignment(SwingConstants.CENTER);
		devField.setFont(new Font("����", Font.PLAIN, 20));
		devField.setBounds(147, 41, 128, 44);
		datepanel.add(devField);
		devField.setColumns(10);
		
		JLabel devdisLabel = new JLabel("\u504F\u79FB\u8DDD\u79BB");
		devdisLabel.setFont(new Font("����", Font.PLAIN, 20));
		devdisLabel.setBounds(306, 41, 86, 41);
		datepanel.add(devdisLabel);
		
		devdisField = new JTextField();
		devdisField.setForeground(Color.RED);
		devdisField.setHorizontalAlignment(SwingConstants.CENTER);
		devdisField.setFont(new Font("����", Font.PLAIN, 20));
		devdisField.setBounds(406, 41, 128, 41);
		datepanel.add(devdisField);
		devdisField.setColumns(10);
		
		JLabel devdirectionLabel = new JLabel("\u504F\u79FB\u65B9\u5411");
		devdirectionLabel.setFont(new Font("����", Font.PLAIN, 20));
		devdirectionLabel.setBounds(47, 117, 86, 41);
		datepanel.add(devdirectionLabel);
		
		devdirectiontextField = new JTextField();
		devdirectiontextField.setForeground(Color.RED);
		devdirectiontextField.setHorizontalAlignment(SwingConstants.CENTER);
		devdirectiontextField.setFont(new Font("����", Font.PLAIN, 20));
		devdirectiontextField.setBounds(147, 115, 128, 44);
		datepanel.add(devdirectiontextField);
		devdirectiontextField.setColumns(10);
		
		JLabel dipangleLabel = new JLabel("\u5782\u5EA6\u89D2");
		dipangleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dipangleLabel.setFont(new Font("����", Font.PLAIN, 20));
		dipangleLabel.setBounds(47, 211, 86, 41);
		datepanel.add(dipangleLabel);
		
		dipangleField = new JTextField();
		dipangleField.setForeground(Color.RED);
		dipangleField.setHorizontalAlignment(SwingConstants.CENTER);
		dipangleField.setFont(new Font("����", Font.PLAIN, 20));
		dipangleField.setBounds(147, 211, 128, 41);
		datepanel.add(dipangleField);
		dipangleField.setColumns(10);
		
		dipangledisField = new JTextField();
		dipangledisField.setForeground(Color.RED);
		dipangledisField.setHorizontalAlignment(SwingConstants.CENTER);
		dipangledisField.setFont(new Font("����", Font.PLAIN, 20));
		dipangledisField.setBounds(406, 211, 128, 41);
		datepanel.add(dipangledisField);
		dipangledisField.setColumns(10);
		
		JLabel dipangledisLabel = new JLabel("\u5782\u5EA6\u8DDD\u79BB");
		dipangledisLabel.setFont(new Font("����", Font.PLAIN, 20));
		dipangledisLabel.setBounds(306, 211, 86, 41);
		datepanel.add(dipangledisLabel);
		
		JLabel dipangledOrenLabel = new JLabel("\u5782\u5EA6\u65B9\u5411");
		dipangledOrenLabel.setFont(new Font("����", Font.PLAIN, 20));
		dipangledOrenLabel.setBounds(47, 287, 86, 41);
		datepanel.add(dipangledOrenLabel);
		
		dipangledOrenField = new JTextField();
		dipangledOrenField.setHorizontalAlignment(SwingConstants.CENTER);
		dipangledOrenField.setForeground(Color.RED);
		dipangledOrenField.setFont(new Font("����", Font.PLAIN, 20));
		dipangledOrenField.setColumns(10);
		dipangledOrenField.setBounds(147, 287, 128, 41);
		datepanel.add(dipangledOrenField);
		//����ͼ��ʾ����
		JPanel linechartpanel = new JPanel();
		linechartpanel.setBorder(new TitledBorder(null, "\u504F\u79FB\u91CF\u6298\u7EBF\u56FE", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		linechartpanel.setBounds(55, 518, 896, 269);
		frame.getContentPane().add(linechartpanel);
	
	}
	/**
	 * �򿪴���
	 * 
	 * @param evt
	 *            ����¼�
	 */
	
	private void openSerialPort(ActionEvent evt) {
		// ��ȡ��������
		String commName = (String) serialportchoosecomboBox.getSelectedItem();
		// ��ȡ�����ʣ�Ĭ��Ϊ4800
		int baudrate = 4800;
		String bps = (String) baudcomboBox.getSelectedItem();
		baudrate = Integer.parseInt(bps);

		// ��鴮�������Ƿ��ȡ��ȷ
		if (commName == null || commName.equals("")) {
			ShowUtils.warningMessage("û����������Ч���ڣ�");
		} else {
			try {
				
			SerialPort.theOnlyPort =	SerialPort.openPort(commName, baudrate);

				if (SerialPort.theOnlyPort != null) {
					towerheightField.setEditable(false);
					angleField.setEditable(false);
					 correctField.setEditable(false);
					serialportbutton.setText("�رմ���");
				}
			} catch (PortInUseException e) {
				ShowUtils.warningMessage("�����ѱ�ռ�ã�");
			}
		}
}
	/**
	 * �رմ���
	 * 
	 * @param evt
	 *            ����¼�
	 */
	private void closeSerialPort(ActionEvent evt) {
		SerialPort.closePort(SerialPort.theOnlyPort);
		towerheightField.setEditable(true);
		angleField.setEditable(true);
		 correctField.setEditable(true);
		 serialportbutton.setText("�򿪴���");
		SerialPort.theOnlyPort = null;
	}
}