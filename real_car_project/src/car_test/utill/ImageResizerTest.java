package car_test.utill;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import car_util.ImageResizer;

/**
 * 유틸 테스트 (ImageResizer)<br><br>
 *
 * 테스트 항목<br><br>
 *
 * 사진 크기 조절<br>
 * 사진 크기 조절 - 작은 사진<br>
 * 사진 크기 조절 - 입력값 오류<br><br>
 *
 * 모든 테스트는 DB 없이 실행됩니다.<br>
 *
 */
class ImageResizerTest {

	/** 테스트용 PNG 사진 만들기 */
	private byte[] createImage(int width, int height) throws IOException {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", baos);
		return baos.toByteArray();
	}// createImage

	/** 테스트용 JLabel 만들기 */
	private JLabel createLabel(int width, int height) {
		JLabel label = new JLabel();
		label.setSize(width, height);
		return label;
	}// createLabel

	@DisplayName("사진 크기 조절")
	@Test
	void testFit() throws IOException {
		// 200x100 사진을 100x100 라벨에 넣으면 비율을 유지해서 100x50
		ImageIcon icon = ImageResizer.fit(createImage(200, 100), createLabel(100, 100));
		assertNotNull(icon);
		assertEquals(100, icon.getIconWidth());
		assertEquals(50, icon.getIconHeight());
	}// testFit

	@DisplayName("사진 크기 조절 - 작은 사진")
	@Test
	void testFitSmall() throws IOException {
		// 50x40 사진을 200x200 라벨에 넣어도 키우지 않고 50x40 그대로
		ImageIcon icon = ImageResizer.fit(createImage(50, 40), createLabel(200, 200));
		assertNotNull(icon);
		assertEquals(50, icon.getIconWidth());
		assertEquals(40, icon.getIconHeight());
	}// testFitSmall

	@DisplayName("사진 크기 조절 - 입력값 오류")
	@Test
	void testFitInvalid() {
		// 사진이 없으면 null
		assertNull(ImageResizer.fit(null, createLabel(100, 100)));
		// 사진이 아닌 데이터면 null
		assertNull(ImageResizer.fit("사진이 아닌 데이터".getBytes(), createLabel(100, 100)));
	}// testFitInvalid

}// ImageResizer