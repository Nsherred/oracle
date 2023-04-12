import { atom, selector, useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { StoryGenerator } from "./StoryGenerator";
import axios from "axios/index";
import * as React from "react";

const testData = {
    "userStory": "As a Car Repair Shop Owner, I want to be able to locate nearby shops so that I can compare prices and services.",
    "acceptanceCriteria": [
        "Car Repair Shop Owner is able to locate nearby shops",
        "Shop locator is able to provide accurate information on prices and services"
    ],
    "decisions": [
        "What criteria should be used to determine the accuracy of the shop locator?",
        "What data should be included in the shop locator?",
        "How should the shop locator be optimized for user experience?"
    ]
}

const formState = atom<UserStoryRequest>({
    key: 'formState'
})

const userStoreResult = selector<UserStoryResult>({
    key: 'UserStoreResult',
    get: async ({ get }) => {
        const state = get(formState)
        const response = await axios({
            headers: {
                "Access-Control-Allow-Origin": "*",
                "Content-Type": "application/json"
            },
            method: 'post',
            url: 'http://localhost:8000/story',
            responseType: 'json',
            data: state
        })
        return response.data
    }
});

interface UserStoryRequest {
    application: string
    feature: string
    persona: string
}


interface UserStoryResult {
    userStory: string
    acceptanceCriteria: string[]
    decisions: string[]
}

const UserStoryDisplay = () => {
    const userStory = useRecoilValue(userStoreResult);
    return <div>
        <h4>User Story</h4>
        <p>{userStory.userStory}</p>
        <h4>Acceptance Criteria</h4>
        <ul>
            {userStory.acceptanceCriteria.map((criterion, index) => <li key={index}>{criterion}</li>)}
        </ul>
        <h4>Decisions</h4>
        <ul>
            {userStory.decisions.map((decision, index) => <li key={index}>{decision}</li>)}
        </ul>
    </div>
}
export const App = () => {
    const setInput = useSetRecoilState(formState);
    return <div className="app">
        <StoryGenerator onSubmit={
            (application, feature, persona) => {
                setInput({
                    application,
                    feature,
                    persona
                })
            }
        }/>
        <React.Suspense fallback={<div>Loading...</div>}>
            <UserStoryDisplay/>
        </React.Suspense>
    </div>
}
