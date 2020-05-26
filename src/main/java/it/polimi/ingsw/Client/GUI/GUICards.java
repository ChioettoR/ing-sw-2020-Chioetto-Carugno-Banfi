package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Model.CardSimplified;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class GUICards {

    Map<String, String> fullImageMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    Map<String, String> smallImageMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    Map<String, String> descriptionMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    ArrayList<String> names = new ArrayList<>() {
        {
            add("Apollo");
            add("Artemis");
            add("Athena");
            add("Atlas");
            add("Chronus");
            add("Demeter");
            add("Hephaestus");
            add("Hestia");
            add("Medusa");
            add("Minotaur");
            add("Pan");
            add("Prometheus");
            add("Triton");
            add("Zeus");
        }
    };

    ArrayList<String> fullImagePath = new ArrayList<>() {
        {
            add("/fullApollo.png");
            add("/fullArtemis.png");
            add("/fullAthena.png");
            add("/fullAtlas.png");
            add("/fullChronus.png");
            add("/fullDemeter.png");
            add("/fullHephaestus.png");
            add("/fullHestia.png");
            add("/fullMedusa.png");
            add("/fullMinotaur.png");
            add("/fullPan.png");
            add("/fullPrometheus.png");
            add("/fullTriton.png");
            add("/fullZeus.png");
        }
    };

    ArrayList<String> smallImagePath = new ArrayList<>() {
        {
            add("/smallApollo.png");
            add("/smallArtemis.png");
            add("/smallAthena.png");
            add("/smallAtlas.png");
            add("/smallChronus.png");
            add("/smallDemeter.png");
            add("/smallHephaestus.png");
            add("/smallHestia.png");
            add("/smallMedusa.png");
            add("/smallMinotaur.png");
            add("/smallPan.png");
            add("/smallPrometheus.png");
            add("/smallTriton.png");
            add("/smallZeus.png");
        }
    };

    public GUICards() {
        for(int i=0; i<names.size(); i++) { fullImageMap.put(names.get(i), fullImagePath.get(i)); }
        for(int i=0; i<names.size(); i++) { smallImageMap.put(names.get(i), smallImagePath.get(i)); }
    }

    public Image getFullImage(String name) {
        return new Image(getClass().getResourceAsStream(fullImageMap.get(name)));
    }

    public Image getSmallImage(String name) {
        return new Image(getClass().getResourceAsStream(smallImageMap.get(name)));
    }

    public void addDescription(CardSimplified cardSimplified) {
        String description = cardSimplified.getDescription();
        description = description.replace("@", " ");
        descriptionMap.put(cardSimplified.getName(), description);
    }

    public String getDescription(String name) {
        return descriptionMap.get(name);
    }
}
