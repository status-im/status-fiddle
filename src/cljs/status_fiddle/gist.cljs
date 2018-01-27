(ns status-fiddle.gist
  (:require [ajax.core :as ajax :refer [GET POST]]
            [cemerick.url :as url]
            [re-frame.core :as re-frame]))

(defn load [id]
  "Finds a gist by id, swaps it into atom's :text key,
   and calls callback when it's given."
  (GET (str "https://api.github.com/gists/" id)
       {:with-credentials? false
        :handler           (fn [x]
                             (let [response-text
                                   (->> (get x "files")
                                        vals
                                        (keep (fn [x] (get x "content")))
                                        first)]
                               (re-frame/dispatch [:load-source response-text])))}))

(defn set-url [url]
  (re-frame/dispatch [:set-url url])
  (set! (.-location js/window) url))

(defn current-url []
  (url/url (-> js/window .-location .-href)))

(defn assoc-anchor [{:keys [anchor] :as url} key value]
  (let [anchor-map (merge (url/query->map anchor)
                          {(name key) (str value)})
        anchor-str (url/map->query anchor-map)]
    (assoc url :anchor anchor-str)))

(defn get-anchor [key]
  (-> (current-url) :anchor url/query->map (get (name key))))

(defn save [string]
  "Saves a gist, and changes url to url#gist={id}
   where id is the gist id."
  (POST "https://api.github.com/gists"
        {:params {:description "???"
                  :public true
                  :files {"fiddle.cljs"
                          {"content" string}}}
         :format :json
         :keywords? true
         :handler (fn [resp]
                    (let [id (get resp "id")]
                      (-> (current-url)
                          (assoc-anchor "gist" id)
                          str
                          set-url)))
         :error-handler (fn [x] (js/console.log x))}))