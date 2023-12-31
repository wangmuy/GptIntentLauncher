# GptIntentLauncher
This is a demo to use LLM like chatGPT to autonomously select and start the intent based on user query.

**Let GPT/chatGPT choose and start the real android intent!**

## Architecture, Stack and Libraries
* MVVM
* Jetpack Compose
* Room
* Koin
* [google-play-scraper](https://github.com/arthur3486/google-play-scraper-kotlin): Used to scrape app's info on Google Play Store
* [llmchain-android](https://github.com/wangmuy/llmchain/tree/android): Langchain port for android

## Showcase
Please watch the demo video below.

https://github.com/wangmuy/GptIntentLauncher/assets/5346962/cdd47a3b-045f-4104-9b7a-6dcf2eeafae3

## TODO
- [x] tool specs make prompt too long, currently filter packages in `LangChainService.getAgentExecutor`, need rephrase
- [x] search tool: simple search using duckduckgo, default return 10 results
- [x] chat history memories
- [x] Add clear chat histories
- [x] use `ConversationBufferMemory` with last 10 rounds of histories
- [ ] maybe use router chains to choose package first, then activity/shortcut/other app specific intents
- [ ] let user add/update/delete app specific intents dynamically!
- [ ] starting shortcut not implemented yet
- [ ] currently scrape no more than `MAX_GET_FROM_STORE` times from play store, maybe remove the limit and somehow notify the scrape progress on ui
- [ ] more resilient `extractToolAndInput` maybe needed
- [ ] better ui

## License
```text
Copyright 2023 wangmuy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
