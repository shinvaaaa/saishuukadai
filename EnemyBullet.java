
import java.awt.*;

class EnemyBullet extends FighterBullet{
    EnemyBullet(){
        dy = 2;
    }
    void move(Graphics buf, int apWidth, int apHeight) {
        if (hp>0) {
          buf.setColor(Color.white);        // gc の色を黒に
          buf.fillOval(x-w, y-h, 2*w, 2*h); // gc を使って・を描く
          if (y > 0 && y < apHeight && x > 0 && x < apWidth) // もし弾が画面内なら
            y = y + dy; // 弾の位置を更新
          else
            hp = 0;     // 画面の外に出たら hp をゼロに
        }
    }
    void revive(int x, int y) { // x, y はFighterの位置
        this.x = x;
        this.y = y;
        hp = 1;         // 発射したら弾をアクティブにする
    }

}