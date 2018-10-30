package cn.iotat.Utils;

import javax.swing.JOptionPane;

public class ShowUtils {

	/**
	 * ��Ϣ��ʾ
	 * 
	 * @param message
	 *            ��Ϣ����
	 */
	public static void message(String message) {
		JOptionPane.showMessageDialog(null, message);
	}

	/**
	 * ������Ϣ��ʾ
	 * 
	 * @param message
	 *            ��Ϣ����
	 */
	public static void warningMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "����",
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * ������Ϣ��ʾ
	 * 
	 * @param message
	 *            ��Ϣ����
	 */
	public static void errorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "����",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * �Զ������Ϣ��ʾ
	 * 
	 * @param title
	 *            ����
	 * @param message
	 *            ��Ϣ����
	 */
	public static void plainMessage(String title, String message) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * ����ѡ���ܵ���ʾ
	 * 
	 * @param title
	 *            ����
	 * @param message
	 *            ��Ϣ����
	 * @return ��/�� 0/1
	 */
	public static boolean selectMessage(String title, String message) {
		int isConfirm = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (0 == isConfirm) {
			return true;
		}
		return false;
	}
}