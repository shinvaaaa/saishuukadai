import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements KeyListener {
  // ■ フィールド変数
  Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics     buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
  Dimension    d;     // アプレットの大きさを管理するオブジェクト
  private int  imgW, imgH; // キャンバスの大きさ
  int C_num;
  private int enmyAnum  = 10; // 敵Ａの数
  private int enmyBnum  = 10; // 敵Bの数
  private int ftrBltNum = 10; // 自機の弾の数
  private int enmBltNum = 10; //敵の弾の数
  private int mode      = -1; // -1: タイトル画面，-2: ゲームオーバー，1〜 ゲームステージ
  private int i, j;
  private int backgroundY = 0;

  Fighter       ftr;  // 自機
  FighterBullet ftrBlt[] = new FighterBullet[ftrBltNum]; // 自機の弾
  EnemyBullet   enmBlt[] = new EnemyBullet[enmBltNum];   // 敵の弾
  EnemyA        enmyA[]  = new EnemyA[enmyAnum];         // 敵機Ａ
  EnemyB        enmyB[]  = new EnemyB[enmyBnum];         // 敵機B
  Image grass = this.getToolkit().getImage("grass.png");
  Image enemyA = this.getToolkit().getImage("enemyA.png");
  Image enemyB = this.getToolkit().getImage("enemyB.png");
  Image chara1 = this.getToolkit().getImage("character1.png");
  Image chara2 = this.getToolkit().getImage("character2.png");
  Image chara3 = this.getToolkit().getImage("character3.png");


  // ■ コンストラクタ
  /**
   * ゲームの初期設定
   * ・描画領域(Image)とGC(Graphics)の作成
   * ・敵，自機，弾オブジェクトの作成
   */
  GameMaster(int imgW, int imgH) { // コンストラクタ （アプレット本体が引数． ゲームの初期化を行う）
    this.imgW = imgW; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    this.imgH = imgH; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    setSize(imgW, imgH); // 描画領域のサイズを設定

    addKeyListener(this);

    ftr = new Fighter(imgW, imgH); // 自機のオブジェクトを実際に作成

    for (i = 0; i < ftrBltNum; i++){       // 自機弾のオブジェクトを実際に作成
      ftrBlt[i] = new FighterBullet();
    }

    for (i = 0; i < enmyAnum; i++) {       // 敵Ａのオブジェクトを実際に作成
      enmyA[i] = new EnemyA(imgW, imgH);
    }

    for (i = 0; i < enmyBnum; i++) {       // 敵Bのオブジェクトを実際に作成
      enmyB[i] = new EnemyB(imgW, imgH);
    }

    for(i=0; i<enmBlt.length; i++){
      enmBlt[i] = new EnemyBullet();
    }
  }

  // ■ メソッド
  // コンストラクタ内で createImage を行うと peer の関連で 
  // nullpointer exception が返ってくる問題を回避するために必要
  public void addNotify(){
    super.addNotify();
    buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
    buf_gc = buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {
    buf_gc.setColor(Color.black);      // gc の色を白に
    buf_gc.fillRect(0, 0, imgW, imgH); // gc を使って白の四角を描く（背景の初期化）
    buf_gc.drawImage(grass, 0, backgroundY, imgW, imgH, this);
    buf_gc.drawImage(grass, 0, backgroundY-imgH, imgW, imgH, this);
    backgroundY += 2;
    if(backgroundY > imgH) backgroundY = 0;

    switch (mode){
    case -2: // ゲームオーバー画面（スペースキーを押されたらタイトル画面）
      buf_gc.setColor(Color.red); // ゲームオーバー画面を描く
      buf_gc.drawString("      == Game over ==      ", imgW/2-80, imgH/2-20);
      buf_gc.drawString("    SPACE キーを押してね♡       ", imgW/2-80, imgH/2+20);
      break;
    case -1: // タイトル画面（スペースキーを押されたらゲーム開始）
      buf_gc.setColor(Color.black); // タイトル画面を描く
      buf_gc.drawString(" == Shooting Game == ", imgW/2-80, imgH/2-20);
      buf_gc.drawString(" ゲームを始めるために", imgW/2-80, imgH/2+20);
      buf_gc.drawString("スペースキーを押してね♡", imgW/2-80, imgH/2+40);
      break;
    case 1://レベル1
      
    default: // ゲーム中
    buf_gc.drawImage(grass, 0, backgroundY, imgW, imgH, this);
    buf_gc.drawImage(grass, 0, backgroundY-imgH, imgW, imgH, this);
    backgroundY += 1;
    if(backgroundY > imgH) backgroundY = 0;
      // *** ランダムに敵を生成 *** 
    makeEnmyA: if (Math.random() < 0.1) { // １０％の確率で一匹生成
	    for (i = 0; i < enmyAnum; i++) {
	      if (enmyA[i].hp == 0) {
	        enmyA[i].revive(imgW, imgH);
	        break makeEnmyA;
	      }
	    }
    }
    makeEnmyB: if (Math.random() < 0.01) { // １０％の確率で一匹生成
      for (i = 0; i < enmyBnum; i++) {
	      if (enmyB[i].hp == 0) {
	        enmyB[i].revive(imgW, imgH);
	        break makeEnmyB;
	      }
	    }
    }

      // *** 自分の弾を発射 ***
      if (ftr.sflag == true && ftr.delaytime == 0) { // もしスペースキーが押されていて＆待ち時間がゼロなら
	      for (i = 0; i < ftrBltNum; i++) {      // 全部の弾に関して前から探査して
	        if (ftrBlt[i].hp == 0) {             // 非アクティブの（死んでいる）弾があれば
	          ftrBlt[i].revive(ftr.x, ftr.y);    // 自機から弾を発射して，
	          ftr.delaytime = 5;                 // 自機の弾発射待ち時間を元に戻して，
	          break;                             // for loop を抜ける
	        }
	      }   
      } else if (ftr.delaytime > 0){            // 弾を発射しない(出来ない)場合は
	        ftr.delaytime--;                       // 待ち時間を１減らす
      }

      //敵の弾を発射
      for(j = 0; j < enmyBnum; j ++){
        if (enmyB[j].hp > 0 && enmyB[j].delaytime == 0) { // HPがゼロでない＆待ち時間がゼロなら
	        for (i = 0; i < enmBlt.length; i++) {      // 全部の弾に関して前から探査して
	          if (enmBlt[i].hp == 0) {             // 非アクティブの（死んでいる）弾があれば
	            enmBlt[i].revive(enmyB[j].x, enmyB[j].y);    // 自機から弾を発射して，
	            enmyB[j].delaytime = 5;
              break;                             // for loop を抜ける
	          }
	        }   
        } else if (enmyB[j].delaytime > 0 && enmyB[j].hp > 0){            // 弾を発射しない(出来ない)場合は
	        enmyB[j].delaytime--;                       // 待ち時間を１減らす
        }
      }
      // ***  敵の弾を発射 ***

    // *** 各オブジェクト間の衝突チェック ***/
    for (i = 0; i < enmyAnum; i++) {
      if (enmyA[i].hp > 0) {                 // 敵が生きていたら
	      ftr.collisionCheck(enmyA[i]);        // 自機と衝突チェック
	      for (j = 0; j < ftrBltNum; j++){      // 全ての自弾に関して
	        if (ftrBlt[j].hp > 0){              // 自弾が生きていたら
	          ftrBlt[j].collisionCheck(enmyA[i]); // 自弾との衝突チェック
          }
        }
	    }
    } 
    
    for (i = 0; i < enmyBnum; i++) {
       if (enmyB[i].hp > 0) {                 // 敵が生きていたら
	      ftr.collisionCheck(enmyB[i]);        // 自機と衝突チェック
	      for (j = 0; j < ftrBltNum; j++)  {
           if (ftrBlt[j].hp > 0){              // 自弾が生きていたら
	            ftrBlt[j].collisionCheck(enmyB[i]); // 自弾との衝突チェック
           }
        }    // 全ての自弾に関して
	    }
    }          // すべての敵に関し，

    for(int k = 0; k < enmBltNum; k ++){
      if(enmBlt[k].hp > 0){
        enmBlt[k].collisionCheck(ftr);
      }
    }

    // *** 自機の生死を判断 ***
    if (ftr.hp < 1){
	    mode = -2; // ゲーム終了
    }

    // *** オブジェクトの描画＆移動 ***
    for (i = 0; i < enmyAnum; i++){
	    enmyA[i].move(buf_gc, imgW, imgH);
      buf_gc.drawImage(enemyA, enmyA[i].x-17, enmyA[i].y-17, 30, 30,  this);

    }

    for (i = 0; i < ftrBltNum; i++){
      enmyB[i].move(buf_gc, imgW, imgH);
      buf_gc.drawImage(enemyB, enmyB[i].x-25, enmyB[i].y-22, 40, 40,  this);
    }

    for (i = 0; i < ftrBltNum; i++){    
	    ftrBlt[i].move(buf_gc, imgW, imgH);
    }

    for (i = 0; i < enmBltNum; i++){    
	    enmBlt[i].move(buf_gc, imgW, imgH);
    }

    ftr.move(buf_gc, imgW, imgH);
    if(C_num == 1) buf_gc.drawImage(chara1,ftr.x-15,ftr.y-15 , 30, 30,  this);
    else if(C_num == 2) buf_gc.drawImage(chara2,ftr.x-15,ftr.y-15 , 30, 30,  this);
    else if(C_num == 3) buf_gc.drawImage(chara3,ftr.x-15,ftr.y-15 , 30, 30,  this);
    // 状態チェック
    for (i = 0; i < enmyAnum; i++) {
	    System.out.print(enmyA[i].hp + " ");
    }
    for (i = 0; i < enmyBnum; i++) {
	    System.out.print(enmyB[i].hp + " ");
    }
    System.out.println("");
  }

  g.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける
}

  // ■ メソッド (Canvas)
  public void update(Graphics gc) { // repaint() に呼ばれる
    paint(gc);
  }

  // ■ メソッド (KeyListener)
  public void keyTyped(KeyEvent ke) {
  } // 今回は使わないが実装は必要

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが押されたら
      ftr.lflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが押されたら
      ftr.rflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_UP: // [↑]キーが押されたら
      ftr.uflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが押されたら
      ftr.dflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_SPACE: // スペースキーが押されたら
      ftr.sflag = true; // フラグを立てる
      if (this.mode != 1){
	      this.mode++;
      }
      ftr.hp = 10;
      break;
    }
  }

  // ■ メソッド (KeyListener)
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが離されたら
      ftr.lflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが離されたら
      ftr.rflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_UP: // [↑]キーが離されたら
      ftr.uflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが離されたら
      ftr.dflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_SPACE: // スペースキーが離されたら
      ftr.sflag = false; // フラグを降ろす
      break;
    }
  }
}


