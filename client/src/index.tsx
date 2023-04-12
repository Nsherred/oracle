import * as React from 'react'
import * as ReactDOM from 'react-dom'
import './global.scss'
import { RecoilRoot } from "recoil";
import { App } from "./App";

ReactDOM.render(
    <RecoilRoot>
        <h3>Story Generator</h3>
        <App/>
    </RecoilRoot>,
    document.getElementById('root')
)
