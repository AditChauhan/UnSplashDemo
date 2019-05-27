/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.Presenter;

import java.util.List;

import tutorial.adit.com.unsplashdemo.model.Urls;

public interface SearchContract {

    interface Views {
        void showData(List<Urls> data);

        void showOffline(List<Urls> data);

        void showProgress();

        void hideProgress();

        void showError();

        boolean adapterIsEmpty();


        void clearAdapter();
    }

    interface Presenter {
        void setLoadingStatus(boolean loading_Status);
        void scrolling();
        void updateQuery(String mquery);
        void isScrolling(boolean status);
        void loadData();
        void setPage(int page);

    }
}
