/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author markburton
 */
public class FontHandler {
    
    private final String url;
    private final Font font;
    
    public FontHandler(String url) throws FontFormatException, IOException {
        this.url = url;
        font = Font.createFont(Font.TRUETYPE_FONT, new File("assets/OpenSans-ExtraBoldItalic.ttf"));
    }
            
    
}
//class DisplayFont {
//    public static void main(String[] args) throws Exception {
//        URL fontUrl = new URL("http://www.webpagepublicity.com/" +
//            "free-fonts/a/Airacobra%20Condensed.ttf");
//        FontHandler font = FontHandler.createFont(FontHandler.TRUETYPE_FONT, fontUrl.openStream());
//        font = font.deriveFont(FontHandler.PLAIN,20);
//        GraphicsEnvironment ge =
//            GraphicsEnvironment.getLocalGraphicsEnvironment();
//        ge.registerFont(font);
//
//        JLabel l = new JLabel(
//            "The quick brown fox jumps over the lazy dog. 0123456789");
//        l.setFont(font);
//        JOptionPane.showMessageDialog(null, l);
//    }
//}