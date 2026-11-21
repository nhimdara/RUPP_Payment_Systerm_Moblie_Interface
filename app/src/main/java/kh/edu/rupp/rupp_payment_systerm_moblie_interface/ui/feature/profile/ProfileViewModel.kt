package kh.edu.rupp.rupp_payment_systerm_moblie_interface.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.AcademicYearResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.model.FacultyResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.MajorResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.remote.dto.StudentResponse
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.ApiResult
import kh.edu.rupp.rupp_payment_systerm_moblie_interface.data.repository.RuppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AcademicEditorState(
    val open: Boolean = false,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val faculties: List<FacultyResponse> = emptyList(),
    val years: List<AcademicYearResponse> = emptyList(),
    val majors: List<MajorResponse> = emptyList(),
    val facultyId: Long? = null,
    val yearId: Long? = null,
    val majorId: Long? = null
)

data class ProfileUiState(
    val loading: Boolean = true,
    val error: String? = null,
    val profile: StudentResponse? = null,
    val editor: AcademicEditorState = AcademicEditorState(),
    val toast: String? = null
)

class ProfileViewModel(private val repo: RuppRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    val darkMode = repo.session.darkMode
    val serverUrl: String get() = repo.session.baseUrl

    init { load() }

    fun load() {
        _state.update { it.copy(loading = it.profile == null, error = null) }
        viewModelScope.launch {
            when (val r = repo.profile()) {
                is ApiResult.Success -> _state.update { it.copy(loading = false, profile = r.data) }
                is ApiResult.Failure -> _state.update { it.copy(loading = false, error = r.message) }
            }
        }
    }

    fun setDarkMode(enabled: Boolean) = repo.session.setDarkMode(enabled)

    fun logout() = repo.logout()

    // ---- Academic info editor (GET faculties / academic-years / majors, PUT my-academic-info) ----
    fun openEditor() {
        val p = _state.value.profile
        _state.update {
            it.copy(
                editor = AcademicEditorState(
                    open = true,
                    loading = true,
                    facultyId = p?.faculty?.id,
                    yearId = p?.academicYear?.id,
                    majorId = p?.major?.id
                )
            )
        }
        viewModelScope.launch {
            val f = async { repo.faculties() }
            val y = async { repo.academicYears() }
            val m = async { repo.majors(p?.faculty?.id) }
            val fr = f.await(); val yr = y.await(); val mr = m.await()
            _state.update {
                it.copy(
                    editor = it.editor.copy(
                        loading = false,
                        faculties = (fr as? ApiResult.Success)?.data.orEmpty(),
                        years = (yr as? ApiResult.Success)?.data.orEmpty(),
                        majors = (mr as? ApiResult.Success)?.data.orEmpty(),
                        error = listOf(fr, yr, mr).filterIsInstance<ApiResult.Failure>().firstOrNull()?.message
                    )
                )
            }
        }
    }

    fun closeEditor() = _state.update { it.copy(editor = AcademicEditorState()) }

    fun selectFaculty(id: Long) {
        _state.update { it.copy(editor = it.editor.copy(facultyId = id, majorId = null, majors = emptyList())) }
        viewModelScope.launch {
            val majors = (repo.majors(id) as? ApiResult.Success)?.data.orEmpty()
            _state.update { it.copy(editor = it.editor.copy(majors = majors)) }
        }
    }

    fun selectYear(id: Long) = _state.update { it.copy(editor = it.editor.copy(yearId = id)) }
    fun selectMajor(id: Long?) = _state.update { it.copy(editor = it.editor.copy(majorId = id)) }

    fun saveEditor(onSaved: () -> Unit) {
        val e = _state.value.editor
        val faculty = e.facultyId ?: return _state.update { it.copy(editor = it.editor.copy(error = "Choose a faculty.")) }
        val year = e.yearId ?: return _state.update { it.copy(editor = it.editor.copy(error = "Choose an academic year.")) }
        _state.update { it.copy(editor = it.editor.copy(saving = true, error = null)) }
        viewModelScope.launch {
            when (val r = repo.updateAcademicInfo(faculty, year, e.majorId)) {
                is ApiResult.Success -> {
                    _state.update { it.copy(profile = r.data, editor = AcademicEditorState(), toast = "Academic info updated") }
                    onSaved()
                }
                is ApiResult.Failure -> _state.update { it.copy(editor = it.editor.copy(saving = false, error = r.message)) }
            }
        }
    }

    fun clearToast() = _state.update { it.copy(toast = null) }
}
