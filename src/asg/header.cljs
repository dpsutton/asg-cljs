(ns asg.header
  (:require ["react-modal" :as Modal]
            [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.colors :as colors]
            [asg.styles.typography :as typo]
            [asg.sections.slack-signup :as slack]
            [cljs-styled-components.reagent :refer [defstyled]]))

(.setAppElement Modal "#app")

(defstyled StyledHeader :header
  {:background colors/primary})

(defstyled HeaderContainer align/Container
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "row"
   :justify-content "space-between"})

(defn Nav []
  [:div (for [event ["events" "recent" "about" "join"]]
          ^{:key event}
          [typo/Navlink {:href (str "#" event)} event])])

(defn header* [modal-state]
  [StyledHeader
   [HeaderContainer
    [typo/Navlink {:href "#events"} "Events"]
    [typo/Navlink {:href "#recent"} "Past Events"]
    [typo/Navlink {:href "#about"} "About"]
    [typo/Navlink {:href "#"
                   :onClick #(reset! modal-state true)} "Slack Signup"]

    [:> Modal {:isOpen @modal-state
               :onRequestClose #(reset! modal-state false)
               :contentLabel "Slack Signup"
               :shouldCloseOnOverlayClick true
               :style {:content {:top         "50%"
                                 :left        "50%"
                                 :right       "auto"
                                 :bottom      "auto"
                                 :marginRight "-50%"
                                 :transform   "translate(-50%, -50%)"}}}
     [slack/Signup]]]])

(defn Header []
  (let [modal-state (r/atom false)]
    [header* modal-state]))
