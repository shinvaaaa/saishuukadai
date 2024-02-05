import java.awt.*;

public class Shootingpro extends Frame implements Runnable {
  // ■ フィールド変数
  Thread     th;   // Thread クラスのオブジェクトを宣言
  GameMaster gm;   // ゲームの進行を担当するクラス
  SelectFighter sf;
  boolean flg1 = false;
  int cW,cH;
  // ■ main メソッド（スタート地点）x
  public static void main(String[] args) {
    new Shootingpro(); // 自分自身のオブジェクトを作成
  }

  // ■ コンストラクタ
  Shootingpro() { // init()は Applet クラスのメソッド
    super("Shooting Game (Sample)"); // 親クラスのコンストラクタを呼び出す
     cW=1000; cH=600;      // キャンバスのサイズ
    this.setSize(cW+30, cH+40);   // フレームのサイズを指定
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // キャンバスをフレームに配置


    
    th = new Thread(this);     // Thread クラスのオブジェクトの作成

    sf = new SelectFighter(cW,cH);
      this.setVisible(false);     // 可視化
      th.start();                // 最後にスレッドを start メソッドで開始

      requestFocusInWindow();    // フォーカスを得る
  }

  // ■ メソッド (Runnable インターフェース用）
  public void run() {
    try {
      while (true) { // 無限ループ
	    Thread.sleep(20); // ウィンドウを更新する前に指定時間だけ休止
      if(sf.flg){
        gm = new GameMaster(cW,cH);// GameMaster クラスのオブジェクトを作成
        gm.setVisible(false);
        this.add(gm);              // キャンバスをフレームに配置
        flg1 = true;
        sf.flg = false;
        this.setVisible(true);
      }
      if(flg1){
        gm.setVisible(true);
        gm.C_num = sf.characternum;
        gm.repaint();     // 再描画を要求する． repaint() は update() を呼び出す
      }
      
      }
    }
    catch (Exception e) {System.out.println("Exception: " + e);}
  }
}


