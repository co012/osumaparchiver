package moe.karczyk.osumaparchiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.nio.file.Paths;
import java.util.ArrayList;

@RequiredArgsConstructor
@Repository
public class MapsRepository {
    private final ArrayList<Paths> mapsPaths = new ArrayList<>();


    public void clear() {
        mapsPaths.clear();
    }

    public void add(Paths path) {
        mapsPaths.add(path);
    }


}
