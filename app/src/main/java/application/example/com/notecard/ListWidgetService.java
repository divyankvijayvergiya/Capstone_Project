package application.example.com.notecard;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Dell on 05-10-2017.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StoryRemoteViewsFactory(this.getApplicationContext(), intent);

    }
}
