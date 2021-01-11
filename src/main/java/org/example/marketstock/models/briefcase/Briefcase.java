package org.example.marketstock.models.briefcase;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.vavr.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.asset.Asset;

/**
 *
 * @author Dominik
 * @since 1.0.0
 */
public class Briefcase implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(Briefcase.class);
    private final Map<Asset, Integer> map;

    public Briefcase(final Map<Asset, Integer> map) {
        this.map = map;
    }
    
    /**
     * Increases number of a given {@link Asset} or adds it to {@link Briefcase}.
     * @param asset - {@link Asset} from {@link Briefcase} or a new {@link Asset}.
     * @param number - Count of asset.
     */
    public void addOrIncrease(final Asset asset, final Integer number) {
        if (contains(asset)) {
            final int initial = map.get(asset);
            final int sum = initial + number;

            map.put(asset, map.get(asset) + number);
            LOGGER.debug("Number of {} was increased from {} to {}.", asset.getName(), initial, sum);
        } else {
            map.put(asset, number);
            LOGGER.debug("{} was added to briefcase; initial count: {}", asset.getName(), number);
        }
    }
    
    /**
     * Reduces number of a given {@link Asset} or removes it when it's count equals 0.
     * @param asset - {@link Asset} that should be in Briefcase.
     * @param number - Count of asset.
     * @return Number of successfully removed assets
     */
    public int decreaseOrRemove(final Asset asset, final Integer number) {
        if (contains(asset)) {
            final int initial = map.get(asset);
            final int difference = initial - number;

            if (difference < 0) {
                LOGGER.debug("Removing {} of {} would result in negative count of asset.", number, asset.getName());
                return 0;
            } else if (difference == 0) {
                map.remove(asset);
                LOGGER.debug("{} was removed from briefcase; last count: {}", asset.getName(), number);
            } else {
                map.put(asset, difference);
                LOGGER.debug("Number of {} was decreased from {} to {}.", asset.getName(), initial, difference);
            }
            return number;
        }
        return 0;
    }

    public boolean contains(final Asset asset) {
        return map.containsKey(asset);
    }

    public boolean contains(final Asset asset, final int numberOfAsset) {
        return contains(asset) && map.get(asset) == numberOfAsset;
    }

    public Stream<Tuple2<Asset, Integer>> stream() {
        return map.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()));
    }

    public List<Asset> getAssets() {
        return new ArrayList<>(map.keySet());
    }

    public List<Integer> getNumbers() {
        return new ArrayList<>(map.values());
    }

    public int getCount(final Asset asset) {
        return map.getOrDefault(asset, 0);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Integer removeEntirely(final Asset asset) {
        return map.remove(asset);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("map", map)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Briefcase)) return false;
        Briefcase briefcase = (Briefcase) o;
        return Objects.equal(map, briefcase.map);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }

    public Map<Asset, Integer> getMap() {
        return map;
    }
}
