package org.example.marketstock.models.briefcase;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.vavr.Tuple2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.marketstock.models.asset.Asset;
import org.example.marketstock.models.briefcase.serialization.BriefcaseDeserializer;
import org.example.marketstock.models.briefcase.serialization.BriefcaseSerializer;

/**
 * Represents an inventory of an {@link org.example.marketstock.models.entity.Entity} that can buy and sell assets.
 * After successful purchase an asset is stored in the briefcase until it is sold.
 *
 * @author Dominik Szmyt
 * @since 1.0.0
 */
@JsonSerialize(using = BriefcaseSerializer.class)
@JsonDeserialize(using = BriefcaseDeserializer.class)
public class Briefcase implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(Briefcase.class);
    private final Map<Asset, Integer> map;

    public Briefcase(final Map<Asset, Integer> map) {
        this.map = map;
    }
    
    /**
     * Increases the number of a given {@link Asset} or adds it to the {@link Briefcase}.
     * @param asset - {@link Asset} from {@link Briefcase} or a new {@link Asset}.
     * @param number - The number of an asset.
     */
    public void addOrIncrease(final Asset asset, final Integer number) {
        if (contains(asset)) {
            final int initial = map.get(asset);
            final int sum = initial + number;

            map.put(asset, map.get(asset) + number);
            LOGGER.debug("[BRIEFCASE]: Number increases from {} to {} of {}.", initial, sum, asset);
        } else {
            map.put(asset, number);
            LOGGER.debug("[BRIEFCASE]: {} added with initial count {}.", asset, number);
        }
    }
    
    /**
     * Reduces the number of a given {@link Asset} or removes it when it's count equals 0.
     * @param asset - {@link Asset} that should be in Briefcase.
     * @param number - The number of an asset.
     * @return The number of successfully removed assets.
     */
    public int decreaseOrRemove(final Asset asset, final Integer number) {
        if (contains(asset)) {
            final int initial = map.get(asset);
            final int difference = initial - number;

            if (difference < 0) {
                LOGGER.debug("[BRIEFCASE]: Aborting decrease in {} because negative difference.", asset);
                return 0; // TODO should return zero because situation is different from not having asset in the first place
            } else if (difference == 0) {
                map.remove(asset);
                LOGGER.debug("[BRIEFCASE]: {} removed with last count {}.", asset, number);
            } else {
                map.put(asset, difference);
                LOGGER.debug("[BRIEFCASE]: Number decreases from {} to {} of {}.", initial, difference, asset);
            }
            return number;
        }
        return 0;
    }

    /**
     * Checks whether an asset is stored in the briefcase.
     * @param asset - An asset that may be stored in the briefcase.
     * @return <tt>true</tt> when an asset is stored in the briefcase, otherwise <tt>false</tt>.
     */
    public boolean contains(final Asset asset) {
        return map.containsKey(asset);
    }

    /**
     * Checks whether a specific number of an asset is stored in the briefcase.
     * @param asset - An asset that may be stored in the briefcase.
     * @param numberOfAsset - The number of an asset.
     * @return <tt>true</tt> when a specific number of an asset is stored in the briefcase, otherwise <tt>false</tt>.
     */
    public boolean contains(final Asset asset, final int numberOfAsset) {
        return contains(asset) && map.get(asset) == numberOfAsset;
    }

    /**
     * Returns the stream of asset-integer tuples.
     * @return The stream of asset-integer tuples.
     * @see Tuple2
     */
    public Stream<Tuple2<Asset, Integer>> stream() {
        return map.entrySet().stream()
                .map(entry -> new Tuple2<>(entry.getKey(), entry.getValue()));
    }

    /**
     * Returns the list of assets stored in the briefcase.
     * @return The list of assets stored in the briefcase.
     */
    public List<Asset> getAssets() {
        return new ArrayList<>(map.keySet());
    }

    /**
     * Returns the list of numbers of each asset stored in the briefcase.
     * @return The list of numbers of each asset stored in the briefcase.
     */
    public List<Integer> getNumbers() {
        return new ArrayList<>(map.values());
    }

    /**
     * Returns the number of an asset. If the asset isn't stored in the briefcase, returns 0.
     * @param asset - An asset that may be stored in the briefcase.
     * @return The number of a specified asset or 0 if it isn't stored in the briefcase.
     */
    public int getCount(final Asset asset) {
        return map.getOrDefault(asset, 0);
    }

    /**
     * Checks whether the briefcase is empty.
     * @return <tt>true</tt> if the briefcase is empty, otherwise <tt>false</tt>.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes an asset from briefcase even if it's number is greater than 0.
     * @param asset - An asset that is to be removed from the briefcase.
     * @return The number of an asset or null if it wasn't stored in the briefcase.
     */
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
