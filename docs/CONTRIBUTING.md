# 🤝 Contributing to TapCraft Pro

## 🎯 **Welcome Contributors!**

Thank you for your interest in contributing to TapCraft Pro! This guide will help you understand our development process, coding standards, and how to submit contributions effectively.

## 📋 **Table of Contents**

1. [Getting Started](#getting-started)
2. [Development Workflow](#development-workflow)
3. [Coding Standards](#coding-standards)
4. [Testing Guidelines](#testing-guidelines)
5. [Pull Request Process](#pull-request-process)
6. [Issue Guidelines](#issue-guidelines)
7. [Community Guidelines](#community-guidelines)
8. [Recognition](#recognition)

---

## 🚀 **Getting Started**

### **Prerequisites**
Before contributing, ensure you have:
- ✅ Read our [Code of Conduct](CODE_OF_CONDUCT.md)
- ✅ Set up the development environment ([Developer Setup Guide](DEVELOPER_SETUP.md))
- ✅ Familiarized yourself with the project structure
- ✅ Joined our [Discord community](https://discord.gg/tapcraft)

### **Types of Contributions**
We welcome various types of contributions:

#### **🐛 Bug Fixes**
- Fix existing bugs and issues
- Improve error handling
- Enhance stability and reliability

#### **✨ New Features**
- Implement new automation actions
- Add integration with new services
- Enhance user interface and experience

#### **📚 Documentation**
- Improve existing documentation
- Add tutorials and guides
- Translate documentation

#### **🧪 Testing**
- Write unit and integration tests
- Improve test coverage
- Add performance tests

#### **🎨 Design & UX**
- Improve user interface design
- Enhance user experience
- Create icons and graphics

#### **🔧 Infrastructure**
- Improve build processes
- Enhance CI/CD pipelines
- Optimize performance

---

## 🔄 **Development Workflow**

### **1. Fork and Clone**
```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/YOUR_USERNAME/tapcraft-pro.git
cd tapcraft-pro

# Add upstream remote
git remote add upstream https://github.com/tapcraft/tapcraft-pro.git
```

### **2. Create Feature Branch**
```bash
# Update your main branch
git checkout main
git pull upstream main

# Create feature branch
git checkout -b feature/your-feature-name

# Or for bug fixes
git checkout -b fix/issue-number-description
```

### **3. Branch Naming Convention**
```
feature/    - New features
fix/        - Bug fixes
docs/       - Documentation updates
test/       - Test improvements
refactor/   - Code refactoring
style/      - Code style changes
perf/       - Performance improvements
chore/      - Maintenance tasks

Examples:
feature/add-slack-integration
fix/script-execution-crash
docs/update-api-documentation
test/add-unit-tests-for-auth
```

### **4. Development Process**
```bash
# Make your changes
# Follow coding standards (see below)

# Run tests frequently
./gradlew test

# Run linting
./gradlew ktlintCheck

# Fix formatting issues
./gradlew ktlintFormat

# Build and test
./gradlew assembleDebug
./gradlew connectedAndroidTest
```

### **5. Commit Guidelines**
Follow [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

#### **Commit Types**
```
feat:     New feature
fix:      Bug fix
docs:     Documentation changes
style:    Code style changes (formatting, etc.)
refactor: Code refactoring
test:     Adding or updating tests
chore:    Maintenance tasks
perf:     Performance improvements
ci:       CI/CD changes
build:    Build system changes
```

#### **Examples**
```bash
# Good commit messages
git commit -m "feat(auth): add biometric authentication support"
git commit -m "fix(script-editor): resolve node connection issue"
git commit -m "docs(api): update authentication endpoints"
git commit -m "test(integration): add Slack integration tests"

# Bad commit messages
git commit -m "fix bug"
git commit -m "update code"
git commit -m "changes"
```

---

## 📝 **Coding Standards**

### **1. Kotlin Style Guide**

#### **Naming Conventions**
```kotlin
// Classes: PascalCase
class ScriptExecutionEngine

// Functions and variables: camelCase
fun executeScript(script: Script): ExecutionResult
val scriptCount = 10

// Constants: SCREAMING_SNAKE_CASE
const val MAX_RETRY_ATTEMPTS = 3

// Package names: lowercase with dots
package com.tapcraft.pro.automation.engine
```

#### **Code Formatting**
```kotlin
// Use 4 spaces for indentation
class ScriptEditor {
    private val nodes = mutableListOf<Node>()
    
    fun addNode(node: Node) {
        nodes.add(node)
        notifyNodeAdded(node)
    }
}

// Line length: 120 characters max
// Break long parameter lists
fun createComplexAutomationScript(
    name: String,
    description: String,
    category: Category,
    nodes: List<Node>,
    connections: List<Connection>
): Script {
    // Implementation
}

// Use trailing commas in multi-line lists
val supportedActions = listOf(
    ActionType.CLICK,
    ActionType.SWIPE,
    ActionType.TEXT_INPUT,
    ActionType.WAIT, // <- trailing comma
)
```

#### **Documentation**
```kotlin
/**
 * Executes an automation script with the specified parameters.
 *
 * @param script The script to execute
 * @param parameters Runtime parameters for the script
 * @param options Execution options (timeout, retry, etc.)
 * @return ExecutionResult containing the outcome and metrics
 * @throws ScriptExecutionException if execution fails
 */
fun executeScript(
    script: Script,
    parameters: Map<String, Any> = emptyMap(),
    options: ExecutionOptions = ExecutionOptions.default()
): ExecutionResult {
    // Implementation
}
```

### **2. Architecture Guidelines**

#### **MVVM Pattern**
```kotlin
// ViewModel
class ScriptEditorViewModel @Inject constructor(
    private val scriptRepository: ScriptRepository,
    private val analyticsService: AnalyticsService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ScriptEditorUiState())
    val uiState: StateFlow<ScriptEditorUiState> = _uiState.asStateFlow()
    
    fun saveScript(script: Script) {
        viewModelScope.launch {
            try {
                scriptRepository.saveScript(script)
                _uiState.update { it.copy(isSaved = true) }
                analyticsService.trackEvent("script_saved")
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
}

// Compose UI
@Composable
fun ScriptEditorScreen(
    viewModel: ScriptEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            // Show error message
        }
    }
    
    // UI implementation
}
```

#### **Repository Pattern**
```kotlin
interface ScriptRepository {
    suspend fun getScripts(): List<Script>
    suspend fun getScript(id: String): Script?
    suspend fun saveScript(script: Script): String
    suspend fun deleteScript(id: String)
}

@Singleton
class ScriptRepositoryImpl @Inject constructor(
    private val localDataSource: ScriptLocalDataSource,
    private val remoteDataSource: ScriptRemoteDataSource,
    private val syncManager: SyncManager
) : ScriptRepository {
    
    override suspend fun getScripts(): List<Script> {
        return try {
            val remoteScripts = remoteDataSource.getScripts()
            localDataSource.saveScripts(remoteScripts)
            remoteScripts
        } catch (e: Exception) {
            localDataSource.getScripts()
        }
    }
}
```

### **3. Error Handling**

#### **Exception Hierarchy**
```kotlin
sealed class TapCraftException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class ScriptExecutionException(
    message: String,
    val scriptId: String,
    cause: Throwable? = null
) : TapCraftException(message, cause)

class NetworkException(
    message: String,
    val statusCode: Int? = null,
    cause: Throwable? = null
) : TapCraftException(message, cause)

class ValidationException(
    message: String,
    val field: String? = null
) : TapCraftException(message)
```

#### **Error Handling Pattern**
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: TapCraftException) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

suspend fun executeScriptSafely(script: Script): Result<ExecutionResult> {
    return try {
        val result = scriptEngine.execute(script)
        Result.Success(result)
    } catch (e: ScriptExecutionException) {
        Result.Error(e)
    } catch (e: Exception) {
        Result.Error(TapCraftException("Unexpected error", e))
    }
}
```

---

## 🧪 **Testing Guidelines**

### **1. Testing Strategy**

#### **Test Pyramid**
```
    /\
   /  \     E2E Tests (Few)
  /____\    
 /      \   Integration Tests (Some)
/________\  Unit Tests (Many)
```

#### **Test Coverage Requirements**
- **Unit Tests**: Minimum 80% coverage
- **Integration Tests**: Critical paths covered
- **UI Tests**: Key user flows covered

### **2. Unit Testing**

#### **Test Structure**
```kotlin
class ScriptExecutionEngineTest {
    
    @Mock
    private lateinit var scriptRepository: ScriptRepository
    
    @Mock
    private lateinit var deviceController: DeviceController
    
    private lateinit var executionEngine: ScriptExecutionEngine
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        executionEngine = ScriptExecutionEngine(
            scriptRepository = scriptRepository,
            deviceController = deviceController
        )
    }
    
    @Test
    fun `executeScript should return success when script executes successfully`() = runTest {
        // Given
        val script = createTestScript()
        val expectedResult = ExecutionResult.Success(
            duration = 1000,
            actionsExecuted = 5
        )
        whenever(deviceController.performAction(any())).thenReturn(true)
        
        // When
        val result = executionEngine.execute(script)
        
        // Then
        assertThat(result).isEqualTo(expectedResult)
        verify(deviceController, times(5)).performAction(any())
    }
    
    @Test
    fun `executeScript should throw exception when device action fails`() = runTest {
        // Given
        val script = createTestScript()
        whenever(deviceController.performAction(any())).thenReturn(false)
        
        // When & Then
        assertThrows<ScriptExecutionException> {
            executionEngine.execute(script)
        }
    }
}
```

#### **Test Utilities**
```kotlin
// TestData.kt
object TestData {
    fun createTestScript(
        id: String = "test-script-1",
        name: String = "Test Script",
        nodes: List<Node> = createTestNodes()
    ): Script {
        return Script(
            id = id,
            name = name,
            nodes = nodes,
            connections = emptyList()
        )
    }
    
    fun createTestNodes(): List<Node> {
        return listOf(
            Node.Click(
                id = "node-1",
                target = Target.Coordinate(100, 200)
            ),
            Node.Wait(
                id = "node-2",
                duration = 1000
            )
        )
    }
}

// TestExtensions.kt
fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    return data as T
}
```

### **3. Integration Testing**

#### **Database Testing**
```kotlin
@RunWith(AndroidJUnit4::class)
class ScriptDaoTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var database: TapCraftDatabase
    private lateinit var scriptDao: ScriptDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TapCraftDatabase::class.java
        ).allowMainThreadQueries().build()
        
        scriptDao = database.scriptDao()
    }
    
    @After
    fun teardown() {
        database.close()
    }
    
    @Test
    fun insertAndGetScript() = runTest {
        // Given
        val script = TestData.createTestScript()
        
        // When
        scriptDao.insertScript(script)
        val retrievedScript = scriptDao.getScript(script.id)
        
        // Then
        assertThat(retrievedScript).isEqualTo(script)
    }
}
```

#### **API Testing**
```kotlin
@RunWith(AndroidJUnit4::class)
class ApiIntegrationTest {
    
    @get:Rule
    val mockWebServer = MockWebServerRule()
    
    private lateinit var apiService: TapCraftApiService
    
    @Before
    fun setup() {
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TapCraftApiService::class.java)
    }
    
    @Test
    fun getScripts_returnsScriptList() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""
                    {
                        "success": true,
                        "data": {
                            "scripts": [
                                {
                                    "id": "script-1",
                                    "name": "Test Script"
                                }
                            ]
                        }
                    }
                """.trimIndent())
        )
        
        // When
        val response = apiService.getScripts()
        
        // Then
        assertThat(response.success).isTrue()
        assertThat(response.data.scripts).hasSize(1)
        assertThat(response.data.scripts[0].name).isEqualTo("Test Script")
    }
}
```

### **4. UI Testing**

#### **Compose Testing**
```kotlin
@RunWith(AndroidJUnit4::class)
class ScriptEditorScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun addNodeButton_clickAddsNode() {
        // Given
        composeTestRule.setContent {
            TapCraftTheme {
                ScriptEditorScreen(
                    onNavigateBack = {},
                    onSaveScript = {}
                )
            }
        }
        
        // When
        composeTestRule
            .onNodeWithText("Add Node")
            .performClick()
        
        composeTestRule
            .onNodeWithText("Click Action")
            .performClick()
        
        // Then
        composeTestRule
            .onNodeWithContentDescription("Click Node")
            .assertExists()
    }
    
    @Test
    fun saveButton_enabledWhenScriptHasNodes() {
        // Test implementation
    }
}
```

---

## 🔄 **Pull Request Process**

### **1. Before Creating PR**

#### **Pre-submission Checklist**
- [ ] Code follows style guidelines
- [ ] All tests pass locally
- [ ] New tests added for new functionality
- [ ] Documentation updated if needed
- [ ] No merge conflicts with main branch
- [ ] Commit messages follow convention
- [ ] PR description is clear and complete

### **2. PR Template**
```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Related Issues
Fixes #(issue number)

## Testing
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed
- [ ] Performance impact assessed

## Screenshots (if applicable)
Add screenshots to help explain your changes.

## Checklist
- [ ] My code follows the style guidelines
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
```

### **3. Review Process**

#### **Review Criteria**
1. **Functionality**: Does the code work as intended?
2. **Code Quality**: Is the code clean, readable, and maintainable?
3. **Testing**: Are there adequate tests?
4. **Performance**: Does it impact performance negatively?
5. **Security**: Are there any security concerns?
6. **Documentation**: Is documentation updated?

#### **Review Timeline**
- **Initial Review**: Within 2 business days
- **Follow-up Reviews**: Within 1 business day
- **Approval**: Requires 2 approvals from maintainers

### **4. Addressing Review Comments**
```bash
# Make requested changes
git add .
git commit -m "fix: address review comments"

# Push changes
git push origin feature/your-feature-name

# PR will automatically update
```

---

## 🐛 **Issue Guidelines**

### **1. Bug Reports**

#### **Bug Report Template**
```markdown
## Bug Description
A clear and concise description of what the bug is.

## Steps to Reproduce
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

## Expected Behavior
A clear and concise description of what you expected to happen.

## Actual Behavior
A clear and concise description of what actually happened.

## Screenshots
If applicable, add screenshots to help explain your problem.

## Environment
- Device: [e.g. Samsung Galaxy S21]
- OS Version: [e.g. Android 12]
- App Version: [e.g. 1.2.3]
- Build Type: [e.g. Debug/Release]

## Additional Context
Add any other context about the problem here.

## Logs
```
Paste relevant logs here
```
```

### **2. Feature Requests**

#### **Feature Request Template**
```markdown
## Feature Description
A clear and concise description of the feature you'd like to see.

## Problem Statement
What problem does this feature solve? What use case does it address?

## Proposed Solution
Describe the solution you'd like to see implemented.

## Alternative Solutions
Describe any alternative solutions or features you've considered.

## Additional Context
Add any other context, mockups, or examples about the feature request here.

## Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2
- [ ] Criterion 3
```

### **3. Issue Labels**

#### **Type Labels**
- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Improvements or additions to documentation
- `question` - Further information is requested
- `help wanted` - Extra attention is needed
- `good first issue` - Good for newcomers

#### **Priority Labels**
- `priority: critical` - Critical issues that block releases
- `priority: high` - High priority issues
- `priority: medium` - Medium priority issues
- `priority: low` - Low priority issues

#### **Component Labels**
- `component: ui` - User interface related
- `component: api` - API related
- `component: database` - Database related
- `component: auth` - Authentication related
- `component: integration` - Third-party integration related

---

## 🌟 **Community Guidelines**

### **1. Code of Conduct**

#### **Our Pledge**
We pledge to make participation in our project a harassment-free experience for everyone, regardless of age, body size, disability, ethnicity, gender identity and expression, level of experience, nationality, personal appearance, race, religion, or sexual identity and orientation.

#### **Our Standards**
Examples of behavior that contributes to creating a positive environment include:
- Using welcoming and inclusive language
- Being respectful of differing viewpoints and experiences
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

### **2. Communication Channels**

#### **Discord Server**
- **#general** - General discussion
- **#development** - Development discussions
- **#help** - Get help with issues
- **#showcase** - Show off your automations
- **#announcements** - Project announcements

#### **GitHub Discussions**
- **Ideas** - Propose new features
- **Q&A** - Ask questions
- **Show and Tell** - Share your work
- **General** - General discussions

### **3. Mentorship Program**

#### **For New Contributors**
- Pair with experienced contributors
- Guided first contributions
- Regular check-ins and support
- Access to private mentorship channel

#### **Becoming a Mentor**
- Minimum 5 merged PRs
- Active community participation
- Commitment to help newcomers
- Application through Discord

---

## 🏆 **Recognition**

### **1. Contributor Levels**

#### **🌱 Newcomer**
- First contribution merged
- Welcome package and Discord role
- Listed in contributors

#### **🚀 Regular Contributor**
- 5+ merged PRs
- Special Discord role
- Priority support
- Early access to features

#### **⭐ Core Contributor**
- 20+ merged PRs
- Significant feature contributions
- Review privileges
- Quarterly recognition

#### **👑 Maintainer**
- Long-term commitment
- Technical leadership
- Merge privileges
- Project direction input

### **2. Recognition Programs**

#### **Monthly Contributor Spotlight**
- Featured on website and social media
- Special Discord announcement
- Contributor interview
- Swag package

#### **Annual Awards**
- **Most Valuable Contributor**
- **Best New Contributor**
- **Most Helpful Community Member**
- **Innovation Award**

### **3. Swag and Rewards**
- Stickers and pins for first contribution
- T-shirts for regular contributors
- Hoodies for core contributors
- Special edition items for maintainers

---

## 📞 **Getting Help**

### **Development Questions**
- **Discord**: #development channel
- **GitHub Discussions**: Q&A section
- **Email**: dev@tapcraft.pro

### **Technical Issues**
- **GitHub Issues**: Bug reports
- **Discord**: #help channel
- **Documentation**: docs.tapcraft.pro

### **Community Support**
- **Discord**: #general channel
- **Forum**: community.tapcraft.pro
- **Reddit**: r/TapCraftPro

---

## 📚 **Resources**

### **Documentation**
- [API Documentation](API_DOCUMENTATION.md)
- [Developer Setup Guide](DEVELOPER_SETUP.md)
- [Deployment Guide](DEPLOYMENT_GUIDE.md)
- [Architecture Overview](ARCHITECTURE.md)

### **Learning Resources**
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Android Development](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Testing Guide](https://developer.android.com/training/testing)

### **Tools**
- [Android Studio](https://developer.android.com/studio)
- [Git](https://git-scm.com/)
- [GitHub CLI](https://cli.github.com/)
- [Postman](https://www.postman.com/) (for API testing)

---

## 🎉 **Thank You!**

Thank you for contributing to TapCraft Pro! Your contributions help make mobile automation accessible to everyone. Together, we're building the future of mobile productivity.

**Happy Coding! 🚀**

---

*For questions about contributing, reach out to us at contribute@tapcraft.pro or join our Discord community.*