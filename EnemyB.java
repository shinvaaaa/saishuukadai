
import java.awt.*;

class EnemyB extends EnemyA {
  int delaytime; // 次の弾を発射するまでの待ち時間
  // コンストラクタ(初期値設定)
 
  EnemyB(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    w = 8;
    h = 8;
    hp = 0;  // 初期状態では全て死亡
    delaytime = 10;
  }
  // ○を描き更新するメソッド
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.green); // gc の色を黒に
    if (hp>0) { // もし生きていれば
      buf.drawRect(x - w, y - h, 2 * w, 2 * h); // gc を使って○を描く
      buf.drawString("HP:" + hp, x-20, y-20);
      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
      if (y>apHeight+h)
	      hp=0;
    }
  }

  void revive(int apWidth, int apHeight) { // 敵を新たに生成（再利用）
    x = (int)(Math.random()*(apWidth-2*w)+w);
    y = -h;
    dy = 1;
    
    if (x<apWidth/2)
      dx = (int)(Math.random()*2);
    else
      dx = -(int)(Math.random()*2);
    hp = 3;
  }

  
}
