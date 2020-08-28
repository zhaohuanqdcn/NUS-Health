package orbroid.nushealth.nushealthapp.ui;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setImageResource((Integer) path);
    }

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
