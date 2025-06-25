# expo-background-uploader

Módulo nativo para upload de arquivos em background no React Native/Expo (Android/iOS), com suporte a múltiplos uploads simultâneos, persistência, notificações, retries e cancelamento.

## Sumário
- [Funcionalidades](#funcionalidades)
- [Instalação](#instalacao)
- [Uso Básico](#uso-basico)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Próximos Passos](#proximos-passos)

## Funcionalidades
- Upload em background (mesmo com app fechado ou minimizado)
- Suporte a múltiplos arquivos e tipos
- Persistência de estado/progresso
- Notificações de progresso/status
- Retry automático e cancelamento
- Headers customizados e autenticação
- API JS moderna e tipada

## Instalação
```bash
npm install expo-background-uploader
# ou
yarn add expo-background-uploader
```

## Uso Básico
```javascript
import BackgroundUploader from 'expo-background-uploader';

await BackgroundUploader.startUpload(filePath, {
  url: 'https://api.exemplo.com/upload',
  headers: { Authorization: 'Bearer token' },
  fieldName: 'file',
});

BackgroundUploader.addProgressListener(progress => {
  console.log(`Upload ${progress.id}: ${progress.percent}%`);
});
```

## Estrutura do Projeto
```
expo-background-uploader/
├── android/
├── ios/
├── src/
│   ├── index.ts
│   └── types.ts
├── package.json
└── README.md
```

## Próximos Passos
- Implementar lógica nativa de upload (WorkManager/URLSession)
- Persistência e notificações
- Documentação detalhada (permissões, exemplos, troubleshooting)
- Testes automatizados

Contribuições são bem-vindas!

- [Expo documentation](https://docs.expo.dev/): Learn fundamentals, or go into advanced topics with our [guides](https://docs.expo.dev/guides).
- [Learn Expo tutorial](https://docs.expo.dev/tutorial/introduction/): Follow a step-by-step tutorial where you'll create a project that runs on Android, iOS, and the web.

## Join the community

Join our community of developers creating universal apps.

- [Expo on GitHub](https://github.com/expo/expo): View our open source platform and contribute.
- [Discord community](https://chat.expo.dev): Chat with Expo users and ask questions.
