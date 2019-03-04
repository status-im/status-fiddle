(ns status-fiddle.utils
  (:require [cemerick.url :as url]))

(defn valid-hiccup? [vec]
  (let [first-element (nth vec 0 nil)]
    (cond
      (not (vector? vec)) false
      (not (pos? (count vec))) false
      (or (vector? first-element) (string? first-element)) false
      (not (reagent.impl.template/valid-tag? first-element)) false
      (not (every? true? (map valid-hiccup? (filter vector? vec)))) false
      :else true)))

(defn current-url []
  (url/url (-> js/window .-location .-href)))

(defn assoc-anchor [{:keys [anchor] :as url} key value]
  (let [anchor-map (merge (url/query->map anchor)
                          {(name key) (str value)})
        anchor-str (url/map->query anchor-map)]
    (assoc url :anchor anchor-str)))

(defn get-anchor [anchor]
  (-> (current-url) :anchor url/query->map (get anchor)))