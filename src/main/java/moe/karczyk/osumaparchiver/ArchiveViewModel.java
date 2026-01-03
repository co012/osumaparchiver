package moe.karczyk.osumaparchiver;

import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.ui.UiCoordinator;
import moe.karczyk.osumaparchiver.ui.UiCoordinatorAware;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchiveViewModel implements UiCoordinatorAware {

    private UiCoordinator uiCoordinator;

    @Override
    public void setUiCoordinator(UiCoordinator uiCoordinator) {
        this.uiCoordinator = uiCoordinator;
    }

    public void onArchiveCompleted() {
        uiCoordinator.notifyArchiveCompleted();
    }
}
