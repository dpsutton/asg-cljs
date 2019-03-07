(ns asg.sections.events
  (:require [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.typography :as typo]
            [cljs-styled-components.reagent :refer [defstyled]]))

(def events-url "https://wnephqc0h5.execute-api.us-east-1.amazonaws.com/prod/events/upcoming")

(comment
  ;; nice that the name is now mostly just "ASG Meeting in
  ;; April". There's no title information.
  {:id        "04g5ofo2p67nvlu1sudqtvlpji",
   :name      "Acadiana Software Group March 2019 Meeting",
   :location  "Lafayette Science Museum, 433 Jefferson St, Lafayette, LA 70501, USA",
   :url       "https://www.google.com/calendar/event?eid=MDRnNW9mbzJwNjdudmx1MXN1ZHF0dmxwamkgbW1xb3ZkbmgycXViZjZsdDBvM3VmMG12dmNAZw",
   :startDate "2019-03-13T23:30:00.000Z",
   :endDate   "2019-03-14T01:00:00.000Z",
   :source    "Acadiana Software Group Calendar"})

(defstyled Title :h3
  {:font-family "press-start"})

(defstyled Info-Body :div
  {:margin-left           "20"
   :display               "grid"
   :align-items "center"
   :grid-template-columns "140px auto"})

(defstyled Info-Row :div
  {:display        "flex"
   :margin-top     "35px"
   :padding        "15px"
   :background     "white"
   :font-family    "press-start"
   :box-shadow     "0 4px 6px 0 hsla(0, 0%, 0%, 0.2)"
   :border-top     "6px solid #437F97"
   :flex-direction "column"
   :margin-left    "15px"})

(def category-style {:font-size    "10px"
                     :color        "grey"
                     :font-weight  "900"
                     :margin-right "35px"})

(defn EventList [events]
  [:ul
   (doall
    (for [{title :name :keys [id location startDate] :as event} events]
      (let [start      (js/Date. startDate)
            start-time (.toLocaleDateString start)]
        ^{:key (:id event)}
        [:li {:style {:list-style-type "none"}}
         [Info-Row
          [Title title]
          [Info-Body
           [:span {:style category-style}
            "Location:"]
           [:span location]
           [:span {:style category-style}
            "Date: "]
           [:span start-time]]]])))])

(defn UpcomingEvents [{:keys [max-events]}]
  (let [state          (r/atom {:stage  :loading
                                :events []})
        now            (js/Date.)
        in-the-future? (fn [{:keys [startDate]}] (> (js/Date. startDate) now))]
    (r/create-class
     {:component-did-mount
      (fn []
        (-> (js/fetch events-url)
            (.then #(.json %))
            (.then #(js->clj % :keywordize-keys true))
            (.then #(:events %))
            (.then (fn [events]
                     (reset! state {:state  :loaded
                                    :events (->> events
                                                 (filter in-the-future?)
                                                 (sort-by :startDate)
                                                 (take max-events))})))
            (.catch (fn [_e]
                      (reset! state {:state :error})))))
      :render
      (fn [{:keys [max-events]}]
        (let [{:keys [stage events]} @state]
          (cond (= stage :loading)
                [:p "Loading events..."]

                (= stage :error)
                [:p "Error loading events..."]

                (-> events count zero?)
                [:p "No upcoming events"]

                (-> events count pos?)
                [EventList events]

                :else
                [:p "Unknown issue with events"])))})))

(defn Events [{:keys [max-events]}]
  [align/Container
   [typo/section-header {:text "Events"
                         :id   "events"}]
   [UpcomingEvents {:max-events max-events}]])
