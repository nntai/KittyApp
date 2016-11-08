package thanhloi.finalproject.HintPlace;

import java.util.List;

public interface PlaceFinderListener {
    void onPlaceFinderStart();
    void onPlaceFinderSuccess(List<Place> route);
}
