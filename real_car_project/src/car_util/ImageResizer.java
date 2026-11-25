package car_util;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 사진을 JLabel 크기에 맞게 비율을 유지하며 줄여주는 클래스
 */
public class ImageResizer {

	private ImageResizer() {
	}

	/**
	 * 사진 byte[]를 label 크기 안에 전체가 다 보이도록(비율 유지) 줄인 ImageIcon으로 반환하는 일
	 * 
	 * @param photo 사진 데이터
	 * @param label 사진을 보여줄 JLabel
	 * @return 크기를 맞춘 ImageIcon, 사진이 없으면 null
	 */
	public static ImageIcon fit(byte[] photo, JLabel label) {
		if (photo == null) {
			return null;
		} // end if

		ImageIcon origin = new ImageIcon(photo);
		int imgW = origin.getIconWidth();
		int imgH = origin.getIconHeight();
		if (imgW <= 0 || imgH <= 0) { // 이미지가 아닌 파일
			return null;
		} // end if

		int boxW = label.getWidth();
		int boxH = label.getHeight();

		// 가로, 세로 중 더 많이 줄여야 하는 쪽에 맞춘다 (원본보다 키우지는 않음)
		double ratio = Math.min(1.0, Math.min((double) boxW / imgW, (double) boxH / imgH));
		int w = Math.max(1, (int) (imgW * ratio));
		int h = Math.max(1, (int) (imgH * ratio));

		return new ImageIcon(origin.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}// fit

}// class