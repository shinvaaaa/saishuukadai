import java.awt.*;
import java.awt.event.*;

public class SelectFighter extends Frame implements ItemListener, ActionListener {
    // ■ フィールド変数
    Checkbox cbx1, cbx2, cbx3;
    Button bt1;
    Label lb1;
    String ss = "";
    boolean flg = false;
    Image buf;
    Graphics buf_gc;
    int characternum;
    private int imgW, imgH;

    Image chara1 = this.getToolkit().getImage("character1.png");
    Image chara2 = this.getToolkit().getImage("character2.png");
    Image chara3 = this.getToolkit().getImage("character3.png");

    // ■ コンストラクタ
    SelectFighter(int imgW, int imgH) {
        super("Select Fighter");
        // フレームの初期設定
        this.imgW = imgW;
        this.imgH = imgH;
        this.setSize(imgW, imgH);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 15));

        // キャンバスの可視化
        CheckboxGroup grp = new CheckboxGroup();

        cbx1 = new Checkbox("キャラクター１", grp, true);
        cbx1.addItemListener(this);
        add(cbx1);

        cbx2 = new Checkbox("キャラクター２", grp, false);
        cbx2.addItemListener(this);
        add(cbx2);

        cbx3 = new Checkbox("キャラクター３", grp, false);
        cbx3.addItemListener(this);
        add(cbx3);

        bt1 = new Button("完了");
        bt1.addActionListener(this);
        add(bt1);

        lb1 = new Label("選択してください");
        add(lb1);

        this.setVisible(true);
        buf = createImage(imgW, imgH);
        buf_gc = buf.getGraphics();
        buf_gc.setColor(Color.black);
        buf_gc.fillRect(0, 0, imgW, imgH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bt1) {
            dispose();
            flg = true;
        }
    }

    // チェックボックスが「操作されると」 itemStateChanged が呼び出される
    public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable() == cbx1) {
            ss = "キャラクター１";
            characternum = 1;
        }
        if (e.getItemSelectable() == cbx2) {
            ss = "キャラクター２";
            characternum = 2;
        }
        if (e.getItemSelectable() == cbx3) {
            ss = "キャラクター３";
            characternum = 3;
        }

        // 画像を描画する前に、buffer をクリア
        buf_gc.setColor(Color.white);
        buf_gc.fillRect(0, 0, imgW, imgH);

        if (e.getItemSelectable() == cbx1 && cbx1.getState()) {
            ss = "キャラクター１";
            buf_gc.drawImage(chara1, 0, 0, this);
        } else if (e.getItemSelectable() == cbx2 && cbx2.getState()) {
            ss = "キャラクター２";
            buf_gc.drawImage(chara2, 0, 0, this);
        } else if (e.getItemSelectable() == cbx3 && cbx3.getState()) {
            ss = "キャラクター３";
            buf_gc.drawImage(chara3, 0, 0, this);
        }

        repaint(); // 描画更新
    }

    public void paint(Graphics g) {
        // (10,70) の座標に文字列表示
        g.drawString(ss, 10, 80);
        // 上部に80pt, 左右と下部に10ptの隙間を開けて四角を描画
        g.fillRect(10, 90, getSize().width - 20, getSize().height - 90);
        g.drawImage(buf, 10, 90, getSize().width - 20, getSize().height - 90, this);
    }
}