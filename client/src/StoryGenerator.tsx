import * as React from 'react'

interface Props {
    onSubmit: (appName: string, feature: string, persona: string) => void
}

export const StoryGenerator = (props: Props) => {
    const [application, setApplication] = React.useState('')
    const [feature, setFeature] = React.useState('')
    const [persona, setPersona] = React.useState('')
    return (
        <form className='story-generator' onSubmit={
            (event) => {
                event.preventDefault()
                props.onSubmit(application, feature, persona)
            }
        }>
            <label>
                Application Name:
                <input type="text" name="application" onInput={
                    (event) => {
                        setApplication(event.currentTarget.value)
                    }
                }/>
            </label>
            <label>
                Feature Name:
                <input type="text" name="feature" onInput={
                    (event) => {
                        setFeature(event.currentTarget.value)
                    }
                }/>
            </label>
            <label>
                Persona:
                <input type="text" name="persona" onInput={
                    (event) => {
                        setPersona(event.currentTarget.value)
                    }
                }/>
            </label>
            <input type="submit" value="Submit"/>
        </form>
    )
}