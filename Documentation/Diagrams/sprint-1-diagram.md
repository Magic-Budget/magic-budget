classDiagram
direction BT
class ApplicationConfiguration {
  + authenticationManager(AuthenticationConfiguration) AuthenticationManager
  + passwordEncoder() PasswordEncoder
  + authenticationProvider() AuthenticationProvider
}
class Build_gradle {
  + main(String[]) Unit
}
class Business {
  + toString() String
   String name
   UUID id
   User user
}
class BusinessController {
  + getAllBusinesses(UUID) ResponseEntity~List~Business~~
  + createBusiness(CreateBusinessRequest, UUID) ResponseEntity~Business~
  + updateBusiness(UUID, UUID, Business) ResponseEntity~Business~
  + getBusinessById(UUID, UUID) ResponseEntity~Business~
  + deleteBusiness(UUID, UUID) ResponseEntity~Void~
}
class BusinessRepository {
<<Interface>>
  + findBusinessByUser(User) List~Business~
}
class BusinessService {
  + getBusinessById(UUID, UUID) Optional~Business~
  + updateBusiness(Business) Business
  + createBusiness(Business) Business
  + getAllBusinesses(UUID) List~Business~
  + deleteBusiness(UUID) void
}
class BusinessServiceTest
class Category {
   String name
   UUID id
}
class CategoryController {
  + getTransactionByCategoryTotals(UUID) ResponseEntity~List~CategoryTotals~~
}
class CategoryRepository {
<<Interface>>
  + findTotalAmountPerCategory(UUID) List~CategoryTotals~
}
class CategoryService {
  + createCategory(Category) Category
  + getTransactionById(UUID) Optional~Category~
  + updateTransaction(Category) Category
  + getCategoryTotals(UUID) List~CategoryTotals~
}
class CategoryTotals
class CreateBusinessRequest {
   String name
}
class CreditDebt {
   UUID id
   BigDecimal interestRate
   Business business
   BigDecimal amount
   User user
}
class CreditDebtController {
  + getCreditDebtsByUserId(UUID) ResponseEntity~List~CreditDebt~~
  + deleteCreditDebt(UUID) ResponseEntity~Void~
  + createCreditDebt(UUID, UUID, CreditDebtCreateRequest) ResponseEntity~CreditDebt~
  + getCreditDebtById(UUID) ResponseEntity~CreditDebt~
  + updateCreditDebt(UUID, UUID, CreditDebtCreateRequest) ResponseEntity~CreditDebt~
  + deleteAllCreditDebtsByUserId(UUID) ResponseEntity~Void~
}
class CreditDebtCreateRequest {
   BigDecimal interestRate
   BigDecimal amount
   UUID businessId
   UUID userId
}
class CreditDebtRepository {
<<Interface>>
  + findByUserId(UUID) List~CreditDebt~
}
class CreditDebtService {
  + deleteAllCreditDebtsByUserId(UUID) void
  + getCreditDebtsByUserId(UUID) List~CreditDebt~
  + getCreditDebtById(UUID) Optional~CreditDebt~
  + updateCreditDebt(CreditDebt) CreditDebt
  + deleteCreditDebt(UUID) void
  + createCreditDebt(CreditDebt, UUID) CreditDebt
}
class CreditDebtServiceTest {
  ~ testGetCreditDebtsByUserId() void
  ~ testDeleteAllCreditDebtsByUserId() void
  ~ testCreateCreditDebtWithInvalidUser() void
  ~ testCreateCreditDebt() void
  ~ testDeleteCreditDebt() void
  ~ testUpdateCreditDebt() void
  ~ testGetCreditDebtById() void
}
class Expense {
   UUID id
   BigDecimal amount
   LocalDateTime dueDate
   User userId
}
class ExpenseController {
  + findExpenseByUserId(UUID) ResponseEntity~List~Expense~~
  + addExpense(String, ExpenseRequest) ResponseEntity~String~
}
class ExpenseRepository {
<<Interface>>
  + findExpenseByUserId(User) List~Expense~
}
class ExpenseRequest {
   LocalDateTime expenseDate
   BigDecimal amount
}
class Income {
   LocalDateTime date
   BigDecimal amount
   User userId
}
class IncomeController {
  + addIncome(String, BigDecimal) ResponseEntity~String~
  + findIncomeByUserId(UUID) ResponseEntity~List~Income~~
}
class IncomeRepository {
<<Interface>>
  + findIncomeById(UUID) List~Income~
  + findIncomeByUserId(User) List~Income~
}
class JwtAuthenticationFilter {
  # doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) void
}
class JwtImplementationService {
  - extractExpirationDate(String) Date
  + generateToken(Map~String, Object~, UserDetails) String
  - isTokenExpired(String) boolean
  + extractUsername(String) String
  - getAllClaims(String) Claims
  - extractSpecificClaim(String, Function~Claims, T~) T
  + validateToken(String, UserDetails) boolean
   Key signatureKey
}
class MagicBudgetApplication {
  + main(String[]) void
}
class MagicBudgetApplicationTests {
  ~ contextLoads() void
}
class RegistrationAndAuthRequest {
  + password() String
  + firstName() String
  + username() String
  + lastName() String
}
class RegistrationAndAuthService {
  + registerUser(RegistrationAndAuthRequest) boolean
  + authenticate(RegistrationAndAuthRequest) String
}
class SavingGoal {
   String name
   UUID id
   BigDecimal amount
   User user
}
class SavingGoalController {
  + getSavingGoalById(UUID) ResponseEntity~SavingGoal~
  + updateSavingGoal(UUID, SavingGoalCreateRequest) ResponseEntity~SavingGoal~
  + getSavingGoalsByid(UUID) ResponseEntity~List~SavingGoal~~
  + createSavingGoal(UUID, SavingGoalCreateRequest) ResponseEntity~SavingGoal~
  + deleteSavingGoal(UUID) ResponseEntity~Void~
}
class SavingGoalCreateRequest {
   String name
   BigDecimal amount
}
class SavingGoalRepository {
<<Interface>>
  + findByUserId(UUID) List~SavingGoal~
}
class SavingGoalService {
  + updateSavingGoal(SavingGoal) SavingGoal
  + getSavingGoalById(UUID) Optional~SavingGoal~
  + deleteAllSavingGoalsByUserId(UUID) void
  + deleteSavingGoal(UUID) void
  + getSavingGoalsByUserId(UUID) List~SavingGoal~
  + createSavingGoal(SavingGoal, UUID) SavingGoal
}
class SavingGoalServiceTest {
  ~ testDeleteSavingGoal() void
  ~ testUpdateSavingGoal() void
  ~ testGetSavingGoalById() void
  ~ testCreateSavingGoalWithInvalidUser() void
  ~ testDeleteAllSavingGoalsByUserId() void
  ~ testCreateSavingGoal() void
  ~ testGetSavingGoalsByUserId() void
}
class SecurityConfiguration {
  + filterChain(HttpSecurity) SecurityFilterChain
}
class Settings_gradle {
  + main(String[]) Unit
}
class Transaction {
   String name
   String description
   LocalDateTime transactionDate
   BigDecimal amount
   UUID id
   LocalDateTime trasnactionDate
   User user
}
class TransactionController {
  + createTransaction(UUID, TransactionCreateRequest) ResponseEntity~Transaction~
  + deleteTransaction(UUID) ResponseEntity~Void~
  + updateTransaction(UUID, TransactionCreateRequest) ResponseEntity~Transaction~
  + getTransactionById(UUID) ResponseEntity~Transaction~
  + getTransactionsByUserId(UUID) ResponseEntity~List~Transaction~~
  + getTransactionsByDateRange(LocalDateTime, LocalDateTime) ResponseEntity~List~Transaction~~
}
class TransactionCreateRequest {
   String name
   String description
   LocalDateTime transactionDate
   BigDecimal amount
}
class TransactionRepository {
<<Interface>>
  + findByTransactionDateBetween(LocalDateTime, LocalDateTime) List~Transaction~
  + findByUserId(UUID) List~Transaction~
}
class TransactionService {
  + updateTransaction(Transaction) Transaction
  + getTransactionsByUserId(UUID) List~Transaction~
  + createTransaction(Transaction) Transaction
  + getTransactionById(UUID) Optional~Transaction~
  + getTransactionsByTransactionDateBetween(LocalDateTime, LocalDateTime) List~Transaction~
}
class TransactionServiceTest {
  ~ testUpdateTransaction() void
  ~ testCreateTransaction() void
  ~ testGetTransactionByGetId() void
  ~ testGetTransactionsByGetUserGetId() void
  ~ testGetTransactionsByGetTrasnactionDateBetween() void
}
class User {
   String password
   String lastName
   Collection~GrantedAuthority~ authorities
   UUID id
   String firstName
   String username
   boolean enabled
   boolean accountNonExpired
   boolean credentialsNonExpired
   boolean accountNonLocked
}
class UserController {
  + getUserById(UUID) ResponseEntity~User~
  + authenticateUser(RegistrationAndAuthRequest) ResponseEntity~String~
  + helloUser() ResponseEntity~String~
  + getUserByUsername(String) ResponseEntity~User~
  + updateUser(UUID, User) ResponseEntity~User~
  + registerUser(RegistrationAndAuthRequest) ResponseEntity~String~
}
class UserDetailsServiceImpl {
  + loadUserByUsername(String) UserDetails
}
class UserRepository {
<<Interface>>
  + findByUsername(String) Optional~User~
  + findUserByUsername(String) UserDetails
}
class UserService {
  + updateUser(User) User
  + getUserByUsername(String) Optional~User~
  + getUserById(UUID) Optional~User~
  + createUser(User) User
}
class UserServiceTest {
  ~ testGetUserByGetId() void
  ~ testUpdateUser() void
  ~ testCreateUser() void
  ~ testGetUserByGetUsername() void
}

ApplicationConfiguration "1" *--> "userDetailsService 1" UserDetailsServiceImpl 
Business "1" *--> "user 1" User 
BusinessController "1" *--> "businessService 1" BusinessService 
BusinessController "1" *--> "userService 1" UserService 
BusinessService "1" *--> "businessRepository 1" BusinessRepository 
BusinessService "1" *--> "userService 1" UserService 
BusinessServiceTest "1" *--> "businessService 1" BusinessService 
CategoryController "1" *--> "categoryService 1" CategoryService 
CategoryService "1" *--> "categoryRepository 1" CategoryRepository 
CreditDebt "1" *--> "business 1" Business 
CreditDebt "1" *--> "user 1" User 
CreditDebtController "1" *--> "businessService 1" BusinessService 
CreditDebtController "1" *--> "creditDebtService 1" CreditDebtService 
CreditDebtController "1" *--> "userService 1" UserService 
CreditDebtService "1" *--> "creditDebtRepository 1" CreditDebtRepository 
CreditDebtService "1" *--> "userService 1" UserService 
CreditDebtServiceTest  ..>  Business : «create»
CreditDebtServiceTest "1" *--> "businessService 1" BusinessService 
CreditDebtServiceTest  ..>  CreditDebt : «create»
CreditDebtServiceTest "1" *--> "creditDebtService 1" CreditDebtService 
CreditDebtServiceTest  ..>  User : «create»
CreditDebtServiceTest "1" *--> "userService 1" UserService 
Expense "1" *--> "userId 1" User 
ExpenseController "1" *--> "expenseRepository 1" ExpenseRepository 
ExpenseController "1" *--> "userService 1" UserService 
Income "1" *--> "userId 1" User 
IncomeController "1" *--> "incomeRepository 1" IncomeRepository 
IncomeController "1" *--> "userService 1" UserService 
JwtAuthenticationFilter "1" *--> "jwtImplementationService 1" JwtImplementationService 
JwtAuthenticationFilter "1" *--> "userDetailsService 1" UserDetailsServiceImpl 
RegistrationAndAuthService "1" *--> "jwtImplementationService 1" JwtImplementationService 
RegistrationAndAuthService "1" *--> "userRepository 1" UserRepository 
SavingGoal "1" *--> "user 1" User 
SavingGoalController "1" *--> "savingGoalService 1" SavingGoalService 
SavingGoalController "1" *--> "userService 1" UserService 
SavingGoalService "1" *--> "savingGoalRepository 1" SavingGoalRepository 
SavingGoalService "1" *--> "userService 1" UserService 
SavingGoalServiceTest  ..>  SavingGoal : «create»
SavingGoalServiceTest "1" *--> "savingGoalService 1" SavingGoalService 
SavingGoalServiceTest  ..>  User : «create»
SavingGoalServiceTest "1" *--> "userService 1" UserService 
SecurityConfiguration "1" *--> "jwtAuthenticationFilter 1" JwtAuthenticationFilter 
Transaction "1" *--> "user 1" User 
TransactionController "1" *--> "transactionService 1" TransactionService 
TransactionController "1" *--> "userService 1" UserService 
TransactionService "1" *--> "transactionRepository 1" TransactionRepository 
TransactionServiceTest  ..>  Transaction : «create»
TransactionServiceTest "1" *--> "transactionService 1" TransactionService 
TransactionServiceTest  ..>  User : «create»
TransactionServiceTest "1" *--> "userService 1" UserService 
UserController "1" *--> "registrationAndAuthService 1" RegistrationAndAuthService 
UserController "1" *--> "userService 1" UserService 
UserDetailsServiceImpl "1" *--> "userRepository 1" UserRepository 
UserService "1" *--> "userRepository 1" UserRepository 
UserServiceTest  ..>  User : «create»
UserServiceTest "1" *--> "userService 1" UserService 
